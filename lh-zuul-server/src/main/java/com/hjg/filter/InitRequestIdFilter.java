package com.hjg.filter;

import com.hjg.util.ThreadContext;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class InitRequestIdFilter extends ZuulFilter {
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
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        String requestId = "[requestId: " + UUID.randomUUID().toString().replace("-", "")  + "]";
        ThreadContext.set("requestId", requestId);
        RequestContext requestContext = RequestContext.getCurrentContext();
        requestContext.addZuulRequestHeader("requestId", requestId);
        return null;
    }

}
