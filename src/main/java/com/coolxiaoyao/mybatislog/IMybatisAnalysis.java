package com.coolxiaoyao.mybatislog;

import com.coolxiaoyao.mybatislog.type.ParamItem;

import java.util.List;

/**
 * @author kerryzhang on 2021/10/29
 */


@Deprecated
public interface IMybatisAnalysis {

    /**
     * 解析
     *
     * @param sql
     * @param params
     * @return
     */
    default String analysisRefExpr(String sql, List<ParamItem> params) {
        return analysisRefExpr(sql, params, null);
    }

    /**
     * resolve
     *
     * @param sql
     * @param params
     * @param dbType
     * @return
     */
    String analysisRefExpr(String sql, List<ParamItem> params, String dbType);

}
