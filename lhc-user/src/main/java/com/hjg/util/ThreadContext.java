package com.hjg.util;

import com.hjg.bean.User;

import java.util.HashMap;
import java.util.Map;

public class ThreadContext {

    private static final ThreadLocal<Map<String, Object>> context = new ThreadLocal<>();
    private static final Map<String, Object> content = new HashMap<>();
    static {
        context.set(content);
    }

    public static void initUser(User user){
        content.put("user", user);
    }

    public static User currentUser(){
        return (User) content.get("user");
    }

    public static void removeUser(){
        content.remove("user");
    }

    public static void initRequestId(String requestId){
        content.put("requestId", requestId);
    }

    public static String requestId(){
        return (String) content.get("requestId");
    }

    public static void removeRequestId(){
        content.remove("requestId");
    }

    public static void set(String key, Object value){
        content.put(key, value);
    }

    public static Object current(String key){
        return content.get(key);
    }

    public static void remove(String key){
        content.remove(key);
    }

    public static void removeAll(){
        content.clear();
    }



}
