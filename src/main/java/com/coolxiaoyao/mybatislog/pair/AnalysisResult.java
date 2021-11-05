package com.coolxiaoyao.mybatislog.pair;

import com.coolxiaoyao.mybatislog.type.ParamItem;

import java.util.List;

/**
 * @author kerryzhang on 2021/10/29
 */


public class AnalysisResult {
    /**
     * 原始未解析的SQL
     */
    private String sourceSqlStr;
    /**
     * 原始未解析的参数
     */
    private String sourceParamStr;
    /**
     * 解析后的参数
     */
    private List<ParamItem> params;

    /**
     * 输出的SQL
     */
    private String formatSqlStr;

    public String getSourceSqlStr() {
        return sourceSqlStr;
    }

    public AnalysisResult setSourceSqlStr(String sourceSqlStr) {
        this.sourceSqlStr = sourceSqlStr;
        return this;
    }

    public String getSourceParamStr() {
        return sourceParamStr;
    }

    public AnalysisResult setSourceParamStr(String sourceParamStr) {
        this.sourceParamStr = sourceParamStr;
        return this;
    }

    public List<ParamItem> getParams() {
        return params;
    }

    public AnalysisResult setParams(List<ParamItem> params) {
        this.params = params;
        return this;
    }

    public String getFormatSqlStr() {
        return formatSqlStr;
    }

    public AnalysisResult setFormatSqlStr(String formatSqlStr) {
        this.formatSqlStr = formatSqlStr;
        return this;
    }
}
