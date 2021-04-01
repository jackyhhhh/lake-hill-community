package com.hjg.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;

@ControllerAdvice
public class ErrorControllerImpl implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

}
