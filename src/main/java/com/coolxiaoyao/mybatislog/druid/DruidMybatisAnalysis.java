package com.coolxiaoyao.mybatislog.druid;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.coolxiaoyao.mybatislog.IMybatisAnalysis;
import com.coolxiaoyao.mybatislog.type.ParamItem;

import java.util.List;
import java.util.Locale;

/**
 * @author kerryzhang on 2021/10/29
 */


@Deprecated
public class DruidMybatisAnalysis implements IMybatisAnalysis {
    private SQLStatement sqlStatement;

    @Override
    public String analysisRefExpr(String sql, List<ParamItem> params, String dbType) {
        DbType type;
        if (dbType == null) {
            type = null;
        } else {
            type = DbType.of(dbType.toLowerCase(Locale.ROOT));
        }
        // druid 解析SQL
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, type);
        sqlStatement = sqlStatements.get(0);
        // if (type == null || type == DbType.mysql || type == DbType.mariadb) {
        MysqlASTHandler handler = new MysqlASTHandler(null);
        sqlStatement.accept(new MysqlASTVisitor(handler));
        // }
        replaceParamToSql(handler.getRefs(), params);
        return sqlStatement.toString();
    }

    private void replaceParamToSql(List<SQLVariantRefExpr> result, List<ParamItem> params) {
        for (int i = 0; i < result.size(); i++) {
            SQLVariantRefExpr variantRefExpr = result.get(i);
            if (i >= params.size()) {
                break;
            }
            ParamItem paramItem = params.get(i);
            SQLExpr newExpr = DruidVariantRefExprUtil.createNewExpr(variantRefExpr, paramItem);
            if (newExpr != null) {
                DruidVariantRefExprUtil.replaceVariantRefExprValue(variantRefExpr, newExpr);
            }
        }
    }
}
