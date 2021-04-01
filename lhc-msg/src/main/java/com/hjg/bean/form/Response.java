package com.hjg.bean.form;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JSONType(orders = {"code", "result", "msg", "obj"})
public class Response {
    private Integer code;
    private String result;
    private String msg;
    private Object obj;

    public Response(){}

    public Response(Integer code, String result, String msg, Object obj) {
        this.code = code;
        this.result = result;
        this.msg = msg;
        this.obj = obj;
    }

    public static Response success(){return new Response(200, "SUCCESS", "OK", 1); }
    public static Response success(Object obj) {return new Response(200, "SUCCESS", "OK", obj); }
    public static Response fail(String msg){return new Response(200, "FAILED", msg, 0); }
    public static Response error(int code, String msg){return new Response(code, "ERROR", msg, 0); }
}
