package com.hjg.aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Slf4j
public class ApiLogAspect {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    @Pointcut("execution(public * com.hjg.controller..*.*(..))")
    public void apiLog(){}

    @Before("apiLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable{
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        log.info("========================================== Start ==========================================");
        log.info("URL           : {}", request.getRequestURL());
        log.info("HTTP METHOD   : {}", request.getMethod());
        log.info("CLASS METHOD  : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        log.info("IP            : {}", request.getRemoteAddr());
        log.info("Request Args  : {}", joinPoint.getArgs());
    }

    @Around("apiLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        log.info("Response Body : {}", JSON.toJSONString(result));
        log.info("Time-Consuming: {}", System.currentTimeMillis() - start);
        log.info("========================================== End ==========================================" + LINE_SEPARATOR);
        return result;
    }


}
