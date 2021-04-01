package com.hjg.filter;

import com.alibaba.fastjson.JSON;
import com.hjg.annotation.LoginFree;
import com.hjg.bean.User;
import com.hjg.bean.form.Response;
import com.hjg.controller.UserController;
import com.hjg.util.HttpUtil;
import com.hjg.util.ThreadContext;
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
import java.util.LinkedHashMap;

@Slf4j
@Component
public class CheckLoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserController userController;
    @Autowired
    private HttpUtil httpUtil;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (! (handler instanceof HandlerMethod)) {
            return true;
        }
        Method method = ((HandlerMethod) handler).getMethod();
        if("error".equals(method.getName()) || method.isAnnotationPresent(LoginFree.class)){
            return true;
        }

        String token = httpUtil.getTokenFromRequest(request);
        if(token != null){
            String json = redisTemplate.boundValueOps(token).get();
            log.info("get userJson from redis with token as key: user={}, getExpire={}", json, redisTemplate.boundValueOps(token).getExpire());
            User user = JSON.parseObject(json, User.class);
            if (user != null) {
                ThreadContext.initUser(user);
                return true;
            }
        }
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(200);
        PrintWriter pw = response.getWriter();
        String json = JSON.toJSONString(Response.error(401, "ACCESS_DENIED: invalid token !"));
        pw.println(json);
        pw.flush();
        pw.close();
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ThreadContext.removeAll();
    }
}
