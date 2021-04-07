package com.hjg.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

@Data
public class LogUtil {

    public static String format(Object... args){
        Object[] vars = new Object[args.length + 1];
        System.arraycopy(args, 0, vars, 0, args.length);
        vars[vars.length-1] = ThreadContext.requestId();
        return JSON.toJSONString(vars, SerializerFeature.WriteMapNullValue);
    }
}
