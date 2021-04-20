package com.hjg.filter;

import com.alibaba.fastjson.JSON;
import com.hjg.annotation.LoginFree;
import com.hjg.bean.User;
import com.hjg.bean.form.Response;
import com.hjg.service.UserService;
import com.hjg.util.LogUtil;
import com.hjg.util.ThreadContext;
import com.hjg.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CheckLoginInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        initRequestId(request);
        if (! (handler instanceof HandlerMethod)) {
            log.debug(LogUtil.format("handler(not HandlerMethod): " + handler.getClass().getName()));
            return true;
        }
        Method method = ((HandlerMethod) handler).getMethod();
        if("error".equals(method.getName()) || method.isAnnotationPresent(LoginFree.class)){
            return true;
        }
        String token = tokenService.getTokenFromRequest(request);
        log.info("拦截器中获得token: {}, {}", token, ThreadContext.requestId());
        if(tokenService.checkToken(token)){
            int uid = Integer.parseInt((tokenService.getMapFromToken(token).get("uid")));
            User user = userService.findByUid(uid);
            if (user != null) {
                ThreadContext.initUser(user);
                redisTemplate.boundValueOps("uid_" + uid).set(JSON.toJSONString(user));
                redisTemplate.boundValueOps("uid_" + uid).expire(30, TimeUnit.MINUTES);
                log.debug(LogUtil.format("uid_"+uid+" => (expire)" + redisTemplate.boundValueOps("uid_" + uid).getExpire()));
            }
            return true;
        }
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(200);
        PrintWriter pw = response.getWriter();
        String json = JSON.toJSONString(Response.error(401, "ACCESS_DENIED: invalid token !"));
        pw.println(json);
        pw.flush();
        pw.close();
        log.info("token验证不通过, 返回401无权限:ACCESS_DENIED: invalid token ! {}", ThreadContext.requestId());
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ThreadContext.removeAll();
    }

    private void initRequestId(HttpServletRequest request){
        String requestId = request.getHeader("requestId");
        log.info("==========>>{} {}, {}",request.getMethod(), request.getRequestURI(), requestId);
        ThreadContext.initRequestId(requestId);
    }
}
