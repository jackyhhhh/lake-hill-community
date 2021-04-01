package com.hjg.util;

import com.alibaba.fastjson.JSON;
import com.hjg.bean.User;
import com.hjg.bean.form.Response;

import java.util.Date;
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

    public static<T> void put(T value){
        content.put(value.getClass().getName(), value);
    }

    public static<T> T current(Class<T> clz){
        Object o = content.get(clz.getName());
        if (clz.isInstance(o)) {
            return clz.cast(o);
        }
        return null;
    }

    public static<T> void remove(Class<T> clz){
        content.remove(clz.getName());
    }

    public static void removeAll(){
        content.clear();
    }


    public static void main(String[] args) {
        Response response = Response.fail("1");
        ThreadContext.put(response);
        ThreadContext.put(new User(7, "jack", "jacky", "nnn", 0, null, null, null));
        System.out.println(JSON.toJSONString(ThreadContext.current(Response.class)));
        System.out.println(JSON.toJSONString(ThreadContext.current(User.class)));
        ThreadContext.put(new User(7, "rose", "rose", "mmm", 1, new Date(), null, null));
        ThreadContext.put(Response.error(545, "lalalla"));
        System.out.println(JSON.toJSONString(ThreadContext.current(User.class)));
        System.out.println(JSON.toJSONString(ThreadContext.current(Response.class)));
    }

}
