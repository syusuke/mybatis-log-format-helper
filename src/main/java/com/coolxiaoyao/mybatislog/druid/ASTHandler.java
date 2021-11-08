package com.coolxiaoyao.mybatislog.druid;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.coolxiaoyao.mybatislog.type.ParamItem;

import java.util.Collections;
import java.util.List;

/**
 * @author kerryzhang on 2021/11/08
 */


public class ASTHandler extends AbstractASTHandler {

    /**
     * 兼容性
     */
    private final List<ParamItem> params;
    /**
     * 兼容性
     */
    private int paramIndex = 0;


    /**
     * 兼容性
     *
     * @param params
     */
    public ASTHandler(List<ParamItem> params) {
        if (params == null) {
            this.params = Collections.emptyList();
        } else {
            this.params = params;
        }
    }

    @Override
    public void handle(SQLObject sqlObject) {
        if (sqlObject instanceof SQLVariantRefExpr) {
            // 直接替换值
            SQLVariantRefExpr variantRefExpr = (SQLVariantRefExpr) sqlObject;
            replaceVariantRefExprValue(variantRefExpr);
        } else if (sqlObject instanceof SQLInListExpr) {
            // 判断 in 列表中是否有 SQLVariantRefExpr
            // (not) in (?,?,?)
            SQLInListExpr inListExpr = (SQLInListExpr) sqlObject;
            List<SQLObject> children = inListExpr.getChildren();
            for (SQLObject child : children) {
                if (child instanceof SQLVariantRefExpr) {
                    replaceVariantRefExprValue((SQLVariantRefExpr) child);
                }
            }
        } else if (sqlObject instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr sqlBinaryOpExpr = (SQLBinaryOpExpr) sqlObject;

            SQLExpr left = sqlBinaryOpExpr.getLeft();
            handle(left);

            SQLExpr right = sqlBinaryOpExpr.getRight();
            handle(right);
        } else if (sqlObject instanceof SQLExistsExpr) {
            SQLExistsExpr sqlExistsExpr = (SQLExistsExpr) sqlObject;
            SQLSelectQuery subSelectQueryQuery = sqlExistsExpr.subQuery.getQuery();
            if (subSelectQueryQuery instanceof SQLSelectQueryBlock) {
                handleSQLSelectQueryBlock((SQLSelectQueryBlock) subSelectQueryQuery);
            } else {
                throw new RuntimeException("no impl SQLExistsExpr" + subSelectQueryQuery.getClass());
            }
        } else if (sqlObject instanceof SQLInSubQueryExpr) {
            // (not) in (select id from table)
            SQLInSubQueryExpr inSubQueryExpr = (SQLInSubQueryExpr) sqlObject;
            SQLSelectQuery subSelectQueryQuery = inSubQueryExpr.subQuery.getQuery();
            if (subSelectQueryQuery instanceof SQLSelectQueryBlock) {
                handleSQLSelectQueryBlock((SQLSelectQueryBlock) subSelectQueryQuery);
            } else {
                throw new RuntimeException("no impl SQLInSubQueryExpr" + subSelectQueryQuery.getClass());
            }
        }
    }


    /**
     * 获取当前参数 Expr 返回一个新的 SQLExpr
     *
     * @param variantRefExpr
     * @return
     */
    @Override
    public SQLExpr getParameterValueExpr(SQLVariantRefExpr variantRefExpr) {
        if (paramIndex >= params.size()) {
            // 超出的应该不解析
            // newExpr = new SQLNullExpr();
            return null;
        } else {
            ParamItem paramItem = params.get(paramIndex);
            paramIndex++;
            return DruidVariantRefExprUtil.createNewExpr(variantRefExpr, paramItem);
        }
    }
}
