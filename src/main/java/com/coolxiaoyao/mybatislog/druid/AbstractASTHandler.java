package com.coolxiaoyao.mybatislog.druid;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.statement.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kerryzhang on 2021/10/27
 */


abstract class AbstractASTHandler implements IVisitorResultRef<SQLVariantRefExpr> {

    private final List<SQLVariantRefExpr> variantRefExprList = new ArrayList<>();


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

    /**
     * TODO 存储过程
     *
     * @param sqlCallStatement
     */
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
            //TODO 要测试过才知道
            /*if (isReplaceParam()) {
                SQLVariantRefExpr variantRefExpr = (SQLVariantRefExpr) expr;
                SQLExpr sqlIdentifierExpr = getParameterValueExpr(variantRefExpr.getName(), true);
                exprTableSource.setExpr(sqlIdentifierExpr);
            }*/
            // OR 下面的方式
            replaceVariantRefExprValue((SQLVariantRefExpr) expr);
        }
    }


    /**
     * 替换变量名,这里按顺序来取值
     *
     * @param variantRefExpr
     */
    protected void replaceVariantRefExprValue(SQLVariantRefExpr variantRefExpr) {
        variantRefExprList.add(variantRefExpr);
        // 参数就是名,一般为 ?
        SQLExpr newExpr = getParameterValueExpr(variantRefExpr);
        if (newExpr != null) {
            DruidVariantRefExprUtil.replaceVariantRefExprValue(variantRefExpr, newExpr);
        }
    }

    public abstract void handle(SQLObject sqlObject);

    /**
     * 获取当前参数 Expr 返回一个新的 SQLExpr,要 isReplaceParam() 返回true
     *
     * @param variantRefExpr
     * @return
     */
    public abstract SQLExpr getParameterValueExpr(SQLVariantRefExpr variantRefExpr);


    @Override
    public List<SQLVariantRefExpr> getRefs() {
        return this.variantRefExprList;
    }
}
