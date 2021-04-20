package com.hjg.exceptions;

import com.hjg.bean.form.Response;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Response accessDeniedExceptionHandler(Exception e){
        return Response.error(401, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response exceptionHandler(Exception e){
        return Response.error(500, e.getMessage());
    }
}
