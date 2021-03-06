package com.coolxiaoyao.mybatislog.druid;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.coolxiaoyao.mybatislog.parser.LogParser;
import com.coolxiaoyao.mybatislog.type.ParamItem;

import java.util.List;

/**
 * @author kerryzhang on 2021/10/27
 */


public class LogDruidParser implements LogParser {

    @Override
    public String parser(String sql, List<ParamItem> params, String dbType) {
        DbType type = DbType.of(dbType);
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, type);
        SQLStatement sqlStatement = sqlStatements.get(0);

        ASTVisitor visitor;
        if (DbType.mysql == type || DbType.mariadb == type) {
            visitor = new MysqlASTVisitor(new MysqlASTHandler(params));
        } else {
            // default
            visitor = new ASTVisitor(new ASTHandler(params));
        }
        sqlStatement.accept(visitor);
        return sqlStatement.toString();
    }
}
