package com.coolxiaoyao.mybatislog.druid;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.coolxiaoyao.mybatislog.type.ParamItem;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kerryzhang on 2021/11/05
 */


public class DruidVariantRefExprUtil {

    /**
     * 替换SQL
     *
     * @param variantRefExpr
     * @param newExpr
     */
    public static void replaceVariantRefExprValue(SQLVariantRefExpr variantRefExpr, SQLExpr newExpr) {
        // 参数就是名,一般为 ?
        // String refExprName = variantRefExpr.getName();
        SQLObject parentObject = variantRefExpr.getParent();
        if (parentObject instanceof SQLExprTableSource) {
            // 表名为参数时: newExpr = SQLIdentifierExpr
            ((SQLExprTableSource) parentObject).replace(variantRefExpr, newExpr);
        } else if (parentObject instanceof SQLBinaryOpExpr) {
            // 一般的 name = ?
            SQLBinaryOpExpr parent = (SQLBinaryOpExpr) parentObject;
            parent.replace(variantRefExpr, newExpr);
        } else if (parentObject instanceof SQLInListExpr) {
            // id in (?,?)
            SQLInListExpr parent = (SQLInListExpr) parentObject;
            parent.replace(variantRefExpr, newExpr);
        } else if (parentObject instanceof SQLInsertStatement.ValuesClause) {
            // 插入
            SQLInsertStatement.ValuesClause vc = (SQLInsertStatement.ValuesClause) parentObject;
            vc.replace(variantRefExpr, newExpr);
        } else if (parentObject instanceof SQLUpdateSetItem) {
            // 更新
            SQLUpdateSetItem updateSetItem = (SQLUpdateSetItem) parentObject;
            updateSetItem.setValue(newExpr);
        }
    }


    public static SQLExpr createNewExpr(SQLVariantRefExpr variantRefExpr, ParamItem paramItem) {
        if (paramItem == null) {
            return new SQLNullExpr();
        }
        Object value = paramItem.getValue();
        if (value == null) {
            return new SQLNullExpr();
        } else {
            // 特殊处理
            if (variantRefExpr.getParent() instanceof SQLExprTableSource) {
                return new SQLIdentifierExpr(value.toString());
            }
            return getExpr(value, paramItem.getCls());
        }
    }


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
