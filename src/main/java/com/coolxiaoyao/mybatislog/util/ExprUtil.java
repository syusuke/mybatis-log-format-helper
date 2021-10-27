package com.coolxiaoyao.mybatislog.util;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kerryzhang on 2021/10/27
 */


public class ExprUtil {

    public static SQLExpr getExpr(Object value, Class<?> cls) {
        if (cls == null || value == null) {
            return new SQLNullExpr();
        }
        if (cls == Integer.class || cls == int.class || cls == Short.class || cls == short.class) {
            return new SQLIntegerExpr(Integer.parseInt(value.toString()));
        } else if (cls == Long.class || cls == long.class) {
            // return new SQLBigIntExpr(Long.parseLong(value.toString()));
            return new SQLIntegerExpr(Long.parseLong(value.toString()));
        } else if (cls == BigInteger.class) {
            return new SQLBigIntExpr(value.toString());
        } else if (cls == BigDecimal.class) {
            return new SQLDecimalExpr(value.toString());
        } else if (cls == Double.class || cls == double.class) {
            return new SQLDoubleExpr(Double.parseDouble(value.toString()));
        } else if (cls == Float.class || cls == float.class) {
            return new SQLFloatExpr(Float.parseFloat(value.toString()));
        } else if (cls == Byte.class || cls == byte.class) {
            return new SQLTinyIntExpr(Byte.valueOf(value.toString()));
        } else if (cls == byte[].class) {
            return new SQLBinaryExpr(value.toString());
        } else if (cls.isArray()) {
            Class<?> itemCls = cls.getComponentType();
            SQLArrayExpr sqlArrayExpr = new SQLArrayExpr();
            Object[] arr = (Object[]) value;
            if (arr.length > 0) {
                List<SQLExpr> values = new ArrayList<>(arr.length);
                for (Object o : arr) {
                    values.add(getExpr(o, itemCls));
                }
                sqlArrayExpr.setValues(values);
            }
            return sqlArrayExpr;
        } else if (List.class.isAssignableFrom(cls)) {
           /* SQLArrayExpr sqlArrayExpr = new SQLArrayExpr();
            List list = (List) value;
            if (list.size() > 0) {
                for (Object o : list) {

                }
            }
            return sqlArrayExpr;*/
        }
        // default String
        return new SQLCharExpr(value.toString());
    }

}
