package com.hjg.service;

import com.hjg.bean.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface TokenService {
    int getExpireTime();
    String getTokenFromRequest(HttpServletRequest request);
    void setTokenInResponse(String token, HttpServletResponse response);
    String createToken(User user);
    Map<String, String> getMapFromToken(String token);
    void signToken(User user, HttpServletResponse response);
    boolean checkToken(String token);
    void invalidToken(String token, HttpServletResponse response);
}
