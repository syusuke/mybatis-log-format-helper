package com.coolxiaoyao.mybatislog.pair;

import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;

import java.util.List;

/**
 * @author kerryzhang on 2021/10/29
 */


public class AnalysisDruidResult extends AnalysisResult {
    private List<SQLVariantRefExpr> refExprList;

    public List<SQLVariantRefExpr> getRefExprList() {
        return refExprList;
    }

    public AnalysisDruidResult setRefExprList(List<SQLVariantRefExpr> refExprList) {
        this.refExprList = refExprList;
        return this;
    }
}
