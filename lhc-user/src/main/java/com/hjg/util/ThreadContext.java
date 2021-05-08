package com.hjg.util;

import com.hjg.bean.User;

import java.util.HashMap;
import java.util.Map;

public class ThreadContext {

    private static final ThreadLocal<Map<String, Object>> context = new ThreadLocal<>();
    private static final Map<String, Object> map = new HashMap<>();
    static {
        context.set(map);
    }

    public static void initUser(User user){
        map.put("user", user);
    }

    public static User currentUser(){
        return (User) map.get("user");
    }

    public static void removeUser(){
        map.remove("user");
    }

    public static void initRequestId(String requestId){
        map.put("requestId", requestId);
    }

    public static String requestId(){
        return (String) map.get("requestId");
    }

    public static void removeRequestId(){
        map.remove("requestId");
    }

    public static void set(String key, Object value){
        map.put(key, value);
    }

    public static Object current(String key){
        return map.get(key);
    }

    public static void remove(String key){
        map.remove(key);
    }

    public static void removeAll(){
        map.clear();
    }



}
