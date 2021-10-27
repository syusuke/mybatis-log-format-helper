package com.coolxiaoyao.mybatislog.visitor;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLInListExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.statement.*;

import java.util.List;

/**
 * @author kerryzhang on 2021/10/27
 */


public abstract class AbstractASTHandler {


    public void handleSQLInsertStatement(SQLInsertStatement sqlInsertStatement) {
        SQLTableSource tableSource = sqlInsertStatement.getTableSource();
        handleSQLTableSource(tableSource);

        List<SQLInsertStatement.ValuesClause> valuesList = sqlInsertStatement.getValuesList();
        for (SQLInsertStatement.ValuesClause valuesClause : valuesList) {
            List<SQLExpr> values = valuesClause.getValues();
            for (SQLExpr value : values) {
                handle(value);
            }
        }
    }

    public void handleSQLDeleteStatement(SQLDeleteStatement sqlDeleteStatement) {
        SQLTableSource tableSource = sqlDeleteStatement.getTableSource();
        handleSQLTableSource(tableSource);
        SQLExpr where = sqlDeleteStatement.getWhere();
        if (where != null) {
            handle(where);
        }
    }


    public void handleSQLUpdateStatement(SQLUpdateStatement sqlUpdateStatement) {
        SQLTableSource tableSource = sqlUpdateStatement.getTableSource();
        handleSQLTableSource(tableSource);
        List<SQLUpdateSetItem> items = sqlUpdateStatement.getItems();
        for (SQLUpdateSetItem item : items) {
            SQLExpr value = item.getValue();
            handle(value);
        }
        SQLExpr where = sqlUpdateStatement.getWhere();
        if (where != null) {
            handle(where);
        }
    }


    public void handleSQLSelectQueryBlock(SQLSelectQueryBlock sqlSelectQueryBlock) {
        // 获取字段列表
        List<SQLSelectItem> selectItems = sqlSelectQueryBlock.getSelectList();
        for (SQLSelectItem selectItem : selectItems) {
            SQLExpr expr = selectItem.getExpr();
            if (expr != null) {
                List<SQLObject> children = expr.getChildren();
                for (SQLObject child : children) {
                    handle(child);
                }
            }
        }
        // 获取表
        SQLTableSource table = sqlSelectQueryBlock.getFrom();
        if (table instanceof SQLExprTableSource) {
            // 普通单表
            handleSQLExprTableSource((SQLExprTableSource) table);
        } else if (table instanceof SQLJoinTableSource) {
            // join多表
            SQLJoinTableSource joinTableSource = (SQLJoinTableSource) table;
            handleSQLTableSource(joinTableSource);
        } else if (table instanceof SQLSubqueryTableSource) {
            // 子查询作为表
            handleSQLTableSource(table);
        }

        // 获取where条件
        SQLExpr where = sqlSelectQueryBlock.getWhere();
        if (where != null) {
            List<SQLObject> children = where.getChildren();
            for (SQLObject child : children) {
                handle(child);
            }
        }

        // 获取分组
        SQLSelectGroupByClause groupBy = sqlSelectQueryBlock.getGroupBy();
        // 获取排序
        SQLOrderBy orderBy = sqlSelectQueryBlock.getOrderBy();
        // 获取分页
        SQLLimit limit = sqlSelectQueryBlock.getLimit();
        if (limit != null) {
            handle(limit.getOffset());
            handle(limit.getRowCount());
        }
    }


    public void handleSQLCallStatement(SQLCallStatement sqlCallStatement) {
        List<SQLExpr> parameters = sqlCallStatement.getParameters();
        for (SQLExpr parameter : parameters) {
            // TODO
        }
    }


    public void handleSQLTableSource(SQLTableSource tableSource) {
        if (tableSource instanceof SQLExprTableSource) {
            handleSQLExprTableSource((SQLExprTableSource) tableSource);
        } else if (tableSource instanceof SQLJoinTableSource) {
            SQLJoinTableSource joinTableSource = (SQLJoinTableSource) tableSource;

            handleSQLTableSource(joinTableSource.getLeft());

            handleSQLTableSource(joinTableSource.getRight());

            handle(joinTableSource.getCondition());
        } else if (tableSource instanceof SQLSubqueryTableSource) {
            SQLSubqueryTableSource subQueryTableSource = (SQLSubqueryTableSource) tableSource;
            SQLSelect select = subQueryTableSource.getSelect();
            handleSQLSelectQueryBlock(select.getQueryBlock());
        }
    }


    /**
     * 表名为变量的处理
     *
     * @param exprTableSource
     */
    public void handleSQLExprTableSource(SQLExprTableSource exprTableSource) {
        SQLExpr expr = exprTableSource.getExpr();
        if (expr instanceof SQLVariantRefExpr) {
            SQLVariantRefExpr variantRefExpr = (SQLVariantRefExpr) expr;
            SQLIdentifierExpr sqlIdentifierExpr = new SQLIdentifierExpr(getIdentifierValue(variantRefExpr.getName()));
            exprTableSource.setExpr(sqlIdentifierExpr);
        }
    }


    /**
     * 替换变量名,这里按顺序来取值
     *
     * @param variantRefExpr
     */
    protected void replaceVariantRefExprValue(SQLVariantRefExpr variantRefExpr) {
        // 参数就是名,一般为 ?
        String refExprName = variantRefExpr.getName();
        SQLExpr newExpr = getParameterValueExpr(refExprName);
        SQLObject parentObject = variantRefExpr.getParent();
        if (parentObject instanceof SQLBinaryOpExpr) {
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


    public abstract void handle(SQLObject sqlObject);

    public abstract SQLExpr getParameterValueExpr(String refExprName);

    /**
     * 返回必须不能为空
     *
     * @param refExprName
     * @return
     */
    public abstract String getIdentifierValue(String refExprName);

}
