package com.hjg.controller;

import com.hjg.annotation.LoginFree;
import com.hjg.bean.form.Response;
import com.hjg.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private HttpUtil httpUtil;

    private static final String SERVICE_PROVIDER = "http://lh-open-user";

    @LoginFree
    @GetMapping("/describe")
    public Response describeHandler(HttpServletRequest request){
        String token = HttpUtil.getTokenFromRequest(request);
        if(token == null) {
            return Response.error(401, "ACCESS_DENIED: invalid token !");
        }
        String url = SERVICE_PROVIDER + "/user/describe";
        return httpUtil.forwardGet(url, request);
    }
}
