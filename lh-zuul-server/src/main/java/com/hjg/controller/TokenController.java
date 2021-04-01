package com.hjg.controller;

import com.hjg.bean.form.Response;
import com.hjg.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping
public class TokenController {

    @Autowired
    private HttpUtil httpUtil;
    private static final String SERVICE_PROVIDER = "http://lhc-user";

    @GetMapping("/checkToken")
    public Response checkTokenHandler(HttpServletRequest request){
        String token = httpUtil.getTokenFromRequest(request);
        if(token == null){
            return Response.error(401, "ACCESS_DENIED: required token but not found !");
        }
        String url = SERVICE_PROVIDER + "/checkToken";
        return httpUtil.forwardGet(url, request);
    }
}
