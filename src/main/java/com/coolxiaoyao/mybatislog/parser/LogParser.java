package com.coolxiaoyao.mybatislog.parser;

import com.coolxiaoyao.mybatislog.type.ParamItem;

import java.util.List;

/**
 * @author kerryzhang on 2021/10/27
 */


public interface LogParser {


    default String parser(String sql, List<ParamItem> params) {
        return parser(sql, params, null);
    }

    /**
     * parser
     *
     * @param sql
     * @param params
     * @param dbType 小写
     */
    String parser(String sql, List<ParamItem> params, String dbType);

}
