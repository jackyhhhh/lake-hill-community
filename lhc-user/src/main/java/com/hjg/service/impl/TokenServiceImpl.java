package com.hjg.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hjg.bean.User;
import com.hjg.service.TokenService;
import com.hjg.util.AesEcbUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    @Value("${jwt.key}")
    private String key;
    @Value("${jwt.expire}")
    private String expire;

    /**
     * 读取配置expire, 计算出毫秒级token有效期
     * @return token 有效期 (秒)
     */
    @Override
    public int getExpireTime(){
        int count = Integer.parseInt(expire.substring(0, expire.length()-1));
        int index = "smhd".indexOf(expire.substring(expire.length()-1));
        int[] unit = {1, 60, 60*60, 60*60*24};
        return count * unit[index];
    }

    @Override
    public String getTokenFromRequest(HttpServletRequest request) {
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

    @Override
    public void setTokenInResponse(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setMaxAge(getExpireTime());
        response.addCookie(cookie);
    }

    @Override
    public String createToken(User user) {
        long now = System.currentTimeMillis();
        long expireAt = now + getExpireTime()*1000;
        Map<String, String> info = new HashMap<>();
        info.put("type", "AES/ECB");
        info.put("expireAt", expireAt + "");
        info.put("uid", user.getUid() + "");
        info.put("username", user.getUsername());
        info.put("nickname", user.getNickname());
        String josn = JSON.toJSONString(info);
        log.debug("createToken: token = " + info.toString());
        log.debug("createToken: expireTime(s) = "+getExpireTime() + "s");
        return AesEcbUtil.encrypt(key, josn);
    }

    @Override
    public Map<String, String> getMapFromToken(String token) {
        if(token != null){
            String json = AesEcbUtil.decrypt(key, token);
            return JSON.parseObject(json, new TypeReference<Map<String, String>>(){});
        }
        return null;
    }

    @Override
    public void signToken(User user, HttpServletResponse response) {
        String token = createToken(user);
        setTokenInResponse(token, response);
    }

    @Override
    public boolean checkToken(String token) {
        Map<String, String> info = getMapFromToken(token);
        if(info != null){
            long expireAt = Long.parseLong(info.get("expireAt"));
            long now = System.currentTimeMillis();
            log.debug("checkToken: token = " + info.toString());
            log.debug("checkToken: (expireAt-now)/1000 = " + (expireAt-now)/1000 +"s");
            return (expireAt - now) > 0;
        }
        return false;
    }

    @Override
    public void invalidToken(String token, HttpServletResponse response) {
        Map<String, String> info = getMapFromToken(token);
        long expireAt = System.currentTimeMillis();
        info.put("expireAt", expireAt+"");
        String invalidToken = AesEcbUtil.encrypt(key, JSON.toJSONString(info));
        setTokenInResponse(invalidToken, response);
        log.debug("invalidToken: token = " + info.toString());
        log.debug("invalidToken: (expireAt-now)/1000 = " + (expireAt-System.currentTimeMillis())/1000 + "s");
    }
}
