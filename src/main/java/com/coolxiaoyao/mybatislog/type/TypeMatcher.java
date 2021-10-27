package com.coolxiaoyao.mybatislog.type;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kerryzhang on 2021/10/27
 */


public class TypeMatcher {

    public static final Map<String, Class<?>> SIMPLE_NAME = new HashMap<>();


    static {

        SIMPLE_NAME.put("null", null);
        defaultRegister();
    }


    private static void defaultRegister() {
        registerClass(String.class);
        registerClass(Byte.class);
        registerClass(byte.class);
        registerClass(Integer.class);
        registerClass(int.class);
        registerClass(Long.class);
        registerClass(long.class);
        registerClass(Short.class);
        registerClass(short.class);
        registerClass(Character.class);
        registerClass(char.class);
        registerClass(Double.class);
        registerClass(double.class);
        registerClass(Float.class);
        registerClass(float.class);
        registerClass(BigDecimal.class);
        registerClass(BigInteger.class);

        registerClass(List.class);
        registerClass(String[].class);
        registerClass(Object[].class);

        // blog
        registerClass(byte[].class);
    }

    public static boolean registerClass(Class<?> cls) {
        boolean b = SIMPLE_NAME.containsKey(cls.getSimpleName());
        if (b) {
            return false;
        }
        SIMPLE_NAME.put(cls.getSimpleName(), cls);
        return true;
    }

    public static Class<?> findClass(String simpleName) {
        Class<?> cls = SIMPLE_NAME.get(simpleName);
        if (cls == null) {
            // 找不到的 默认 String
            return String.class;
        }
        return cls;
    }

}
