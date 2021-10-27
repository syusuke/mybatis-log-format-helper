package com.coolxiaoyao.mybatislog.pair;

/**
 * @author kerryzhang on 2021/10/27
 */


public class SqlParamPair {
    /**
     * 原始SQL 字符串
     */
    private final String sql;
    /**
     * 原始SQL参数的字符串
     */
    private final String param;

    public SqlParamPair(String sql, String param) {
        this.sql = sql;
        this.param = param;
    }

    public String getSql() {
        return sql;
    }

    public String getParam() {
        return param;
    }

    @Override
    public String toString() {
        return "SqlParamPair{" +
                "sql='" + sql + '\'' +
                ", param='" + param + '\'' +
                '}';
    }
}
