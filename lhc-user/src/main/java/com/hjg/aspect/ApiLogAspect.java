package com.hjg.aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class ApiLogAspect {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    /** 以 controller 包下定义的所有请求为切入点 */
    @Pointcut("execution(public * com.hjg.controller..*.*(..))")
    public void apiLog() {}
    /**
     * 在切点之前织入	
     * @param joinPoint 切点
     * @throws Throwable
     */
    @Before("apiLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 开始打印请求日志	
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 打印请求相关参数	
        log.info("========================================== Start ==========================================");
        // 打印请求 url	
        log.info("URL            : {}", request.getRequestURL().toString());
        // 打印 Http method	
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法	
        log.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        // 打印请求的 IP	
        log.info("IP             : {}", request.getRemoteAddr());
        log.info("Request Args   : {}", joinPoint.getArgs());
    }
    /**
     * 在切点之后织入	
     * @throws Throwable
     */
    @After("apiLog()")
    public void doAfter() throws Throwable {
    }
    /**
     * 环绕 , 定义执行切点的时机, 织入该时机前后的代码
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("apiLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
//        // 打印请求入参
//        log.info("Request Args  : {}", JSON.toJSONString(proceedingJoinPoint.getArgs()));
        // 打印出参
        log.info("Response Args  : {}", JSON.toJSONString(result));
        // 执行耗时
        log.info("Time-Consuming : {} ms", System.currentTimeMillis() - startTime);
        log.info("=========================================== End ===========================================" + LINE_SEPARATOR);
        return result;
    }
}
