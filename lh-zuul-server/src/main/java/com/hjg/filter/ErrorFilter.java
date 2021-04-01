package com.hjg.filter;

import com.alibaba.fastjson.JSON;
import com.hjg.bean.form.Response;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class ErrorFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("-------------------------{}{}-----run--------------------------", filterType(), filterOrder());
        RequestContext ctx = RequestContext.getCurrentContext();
        Throwable throwable = ctx.getThrowable();
        HttpServletResponse resp = ctx.getResponse();
        Response response;
        if (throwable != null) {
            String errorMsg = throwable.getCause().getMessage();
            log.error("this is a throwable: {}", errorMsg);
            ctx.setSendZuulResponse(false);
            if (errorMsg != null && errorMsg.startsWith("ACCESS_DENIED")) {
                ctx.set("error.status_code", HttpServletResponse.SC_UNAUTHORIZED);
                resp.setStatus(200);
                response = Response.error(401, errorMsg);
            } else{
                ctx.set("error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.setStatus(500);
                response = Response.error(500, errorMsg == null ? "INTERNAL_SERVER_ERROR" : errorMsg);
            }
            ctx.set("error.exception", throwable.getCause());
            try (PrintWriter pw = resp.getWriter()){
                resp.setContentType("application/json;charset=UTF-8");
                pw.println(JSON.toJSONString(response));
                pw.flush();
                log.info("send response success: {}", response.toString());
            } catch (IOException e){
                log.error("ErrorFilter throw an IOException: {}", e.getCause().getMessage());
            }
        }
        return null;
    }
}
