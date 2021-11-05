package com.coolxiaoyao.mybatislog.druid;

import com.alibaba.druid.sql.ast.statement.SQLCallStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;

/**
 * @author kerryzhang on 2021/10/27
 */


public class MysqlASTVisitor extends MySqlASTVisitorAdapter {
    private final MysqlASTHandler handler;

    MysqlASTVisitor(MysqlASTHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean visit(MySqlInsertStatement x) {
        handler.handleSQLInsertStatement(x);
        return super.visit(x);
    }

    @Override
    public boolean visit(MySqlDeleteStatement x) {
        handler.handleSQLDeleteStatement(x);
        return super.visit(x);
    }

    @Override
    public boolean visit(MySqlUpdateStatement x) {
        handler.handleSQLUpdateStatement(x);
        return super.visit(x);
    }

    @Override
    public boolean visit(MySqlSelectQueryBlock x) {
        handler.handleSQLSelectQueryBlock(x);
        return super.visit(x);
    }

    @Override
    public boolean visit(SQLCallStatement x) {
        // 存储过程
        handler.handleSQLCallStatement(x);
        return super.visit(x);
    }
}
