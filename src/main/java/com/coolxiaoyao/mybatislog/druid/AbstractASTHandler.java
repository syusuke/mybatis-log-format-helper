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
        // ??????????????????
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
        // ?????????
        SQLTableSource table = sqlSelectQueryBlock.getFrom();
        if (table instanceof SQLExprTableSource) {
            // ????????????
            handleSQLExprTableSource((SQLExprTableSource) table);
        } else if (table instanceof SQLJoinTableSource) {
            // join??????
            SQLJoinTableSource joinTableSource = (SQLJoinTableSource) table;
            handleSQLTableSource(joinTableSource);
        } else if (table instanceof SQLSubqueryTableSource) {
            // ??????????????????
            handleSQLTableSource(table);
        }

        // ??????where??????
        SQLExpr where = sqlSelectQueryBlock.getWhere();
        if (where != null) {
            List<SQLObject> children = where.getChildren();
            for (SQLObject child : children) {
                handle(child);
            }
        }

        // ????????????
        SQLSelectGroupByClause groupBy = sqlSelectQueryBlock.getGroupBy();
        // ????????????
        SQLOrderBy orderBy = sqlSelectQueryBlock.getOrderBy();
        // ????????????
        SQLLimit limit = sqlSelectQueryBlock.getLimit();
        if (limit != null) {
            handle(limit.getOffset());
            handle(limit.getRowCount());
        }
    }

    /**
     * TODO ????????????
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
     * ????????????????????????
     *
     * @param exprTableSource
     */
    public void handleSQLExprTableSource(SQLExprTableSource exprTableSource) {
        SQLExpr expr = exprTableSource.getExpr();
        if (expr instanceof SQLVariantRefExpr) {
            //TODO ?????????????????????
            /*if (isReplaceParam()) {
                SQLVariantRefExpr variantRefExpr = (SQLVariantRefExpr) expr;
                SQLExpr sqlIdentifierExpr = getParameterValueExpr(variantRefExpr.getName(), true);
                exprTableSource.setExpr(sqlIdentifierExpr);
            }*/
            // OR ???????????????
            replaceVariantRefExprValue((SQLVariantRefExpr) expr);
        }
    }


    /**
     * ???????????????,????????????????????????
     *
     * @param variantRefExpr
     */
    protected void replaceVariantRefExprValue(SQLVariantRefExpr variantRefExpr) {
        variantRefExprList.add(variantRefExpr);
        // ???????????????,????????? ?
        SQLExpr newExpr = getParameterValueExpr(variantRefExpr);
        if (newExpr != null) {
            DruidVariantRefExprUtil.replaceVariantRefExprValue(variantRefExpr, newExpr);
        }
    }

    public abstract void handle(SQLObject sqlObject);

    /**
     * ?????????????????? Expr ?????????????????? SQLExpr,??? isReplaceParam() ??????true
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
