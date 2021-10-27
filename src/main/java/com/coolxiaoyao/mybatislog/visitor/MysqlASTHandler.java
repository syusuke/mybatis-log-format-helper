package com.coolxiaoyao.mybatislog.visitor;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.coolxiaoyao.mybatislog.type.ParamItem;
import com.coolxiaoyao.mybatislog.util.ExprUtil;

import java.util.List;

/**
 * @author kerryzhang on 2021/10/27
 */


class MysqlASTHandler extends AbstractASTHandler {
    private final List<ParamItem> params;
    private int paramIndex = 0;

    public MysqlASTHandler(List<ParamItem> params) {
        this.params = params;
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
     * @param refExprName 这里统一为 ?
     * @return
     */
    @Override
    public SQLExpr getParameterValueExpr(String refExprName) {
        SQLExpr newExpr;
        if (paramIndex >= params.size()) {
            newExpr = new SQLNullExpr();
        } else {
            ParamItem paramItem = params.get(paramIndex++);
            if (paramItem.getValue() == null) {
                newExpr = new SQLNullExpr();
            } else {
                newExpr = ExprUtil.getExpr(paramItem.getValue(), paramItem.getCls());
            }
        }
        return newExpr;
    }


    /**
     * 返回必须不能为空
     *
     * @param refExprName
     * @return
     */
    @Override
    public String getIdentifierValue(String refExprName) {
        ParamItem paramItem = params.get(paramIndex++);
        return paramItem.getValue().toString();
    }
}
