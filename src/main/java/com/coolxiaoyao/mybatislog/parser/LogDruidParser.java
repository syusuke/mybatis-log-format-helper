package com.coolxiaoyao.mybatislog.parser;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.coolxiaoyao.mybatislog.type.ParamItem;
import com.coolxiaoyao.mybatislog.visitor.MysqlASTVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author kerryzhang on 2021/10/27
 */


public class LogDruidParser implements LogParser {
    private static final Logger logger = LoggerFactory.getLogger(LogDruidParser.class);

    @Override
    public String parser(String sql, List<ParamItem> params, String dbType) {
        DbType type = DbType.of(dbType);
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, type);
        SQLStatement sqlStatement = sqlStatements.get(0);
        if (type == DbType.mysql) {
            sqlStatement.accept(new MysqlASTVisitor(params));
        }
        return sqlStatement.toString();
    }
}
