package com.hjg.util;

import com.hjg.bean.User;
import com.hjg.bean.form.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class HttpUtil {

    @Autowired
    private RestTemplate restTemplate;

    public static String getTokenFromRequest(HttpServletRequest request) {
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

    public HttpEntity<?> setTokenInHttpEntity(Object requestBody, HttpServletRequest request){
        String token = getTokenFromRequest(request);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "token=" + token);
        String requestId = ThreadContext.requestId();
        requestHeaders.add("requestId", requestId);
        return new HttpEntity<>(requestBody, requestHeaders);
    }

    public Response forwardGet(String url, HttpServletRequest request){
        HttpEntity<?> requestEntity = setTokenInHttpEntity(null, request);
        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, Response.class).getBody();
    }

    public Response forwardGet(String url){
        return restTemplate.getForObject(url, Response.class);
    }

    public Response forwardPost(String url, Object requestBody){
        return restTemplate.postForObject(url, requestBody, Response.class);
    }

    public Response forwardPost(String url, Object requestBody, HttpServletRequest request){
        HttpEntity<?> requestEntity = setTokenInHttpEntity(requestBody, request);
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, Response.class).getBody();
    }

    public Response forwardLogin (String url, User requestBody, HttpServletResponse response){
        HttpEntity<User> requestEntity = new HttpEntity<>(requestBody);
        ResponseEntity<Response> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Response.class);
        List<String> strings = responseEntity.getHeaders().get("Set-Cookie");
        if( strings != null){
            for (String s : strings) {
                if (s.startsWith("token=")) {
                    response.addHeader("Set-Cookie", s);
                    break;
                }
            }
        }
        return responseEntity.getBody();
    }

    public Response forwardLogout(String url, HttpServletRequest request, HttpServletResponse response){
        HttpEntity<?> requestEntity = setTokenInHttpEntity(null, request);
        ResponseEntity<Response> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Response.class);
        List<String> strings = responseEntity.getHeaders().get("Set-Cookie");
        if( strings != null){
            for (String s : strings) {
                if (s.startsWith("token=")) {
                    response.addHeader("Set-Cookie", s);
                    break;
                }
            }
        }
        return responseEntity.getBody();
    }
}
