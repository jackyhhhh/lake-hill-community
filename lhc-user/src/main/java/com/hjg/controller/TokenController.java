package com.hjg.controller;

import com.hjg.bean.form.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class TokenController {

    @GetMapping("/checkToken")
    public Response checkTokenHandler(){
        return Response.success();
    }
}
