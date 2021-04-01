package com.hjg.filter;

import com.alibaba.fastjson.JSON;
import com.hjg.bean.User;
import com.hjg.bean.form.Response;
import com.hjg.controller.UserController;
import com.hjg.exceptions.AccessDeniedException;
import com.hjg.util.HttpUtil;
import com.hjg.util.ThreadContext;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CheckLoginStatusFilter extends ZuulFilter {
    @Autowired
    private UserController userController;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        log.info("-------------------------{}{}-------shouldFilter()------------------------", filterType(), filterOrder());
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        String uri = request.getRequestURI();
        boolean shouldFilter = ! uri.startsWith("/user/");
        log.info("{}, URI: {}, shouldFilter= {}", request.getMethod(), uri, shouldFilter);
        return shouldFilter;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("-------------------------{}{}-----run()--------------------------", filterType(), filterOrder());
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = getTokenFromRequest(request);
        log.info("CheckLoginStatusFilter: token= {}", token);
        if (token == null) {
            ctx.setSendZuulResponse(false);
            responseForAccessDenied("ACCESS_DENIED: require token but not found !");
            return null;
        }
        Response res = userController.describeHandler(request);
        if (res.getCode() == 401) {
            ctx.setSendZuulResponse(false);
            responseForAccessDenied("ACCESS_DENIED: invalid token !");
            return null;
        }
        Object data = res.getObj();
        String json = JSON.toJSONString(data);
        redisTemplate.boundValueOps(token).set(json, 10, TimeUnit.SECONDS);
        log.info("set userJson into redis with token as key: " + redisTemplate.boundValueOps(token).get());
        return null;
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        if(request != null){
            Cookie[] cookies = request.getCookies();
            if (cookies != null && cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        return cookie.getValue();
                    }
                }
            }
        }
        return null;
    }

    private void responseForAccessDenied(String errorMsg){
        HttpServletResponse response = RequestContext.getCurrentContext().getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(200);
        try (PrintWriter pw = response.getWriter()) {
            String json = JSON.toJSONString(Response.error(401, errorMsg));
            pw.println(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}