package com.coolxiaoyao.mybatislog.druid;

import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.visitor.SQLASTVisitorAdapter;

/**
 * @author kerryzhang on 2021/11/08
 */


public class ASTVisitor extends SQLASTVisitorAdapter {


    private final ASTHandler handler;

    ASTVisitor(ASTHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean visit(SQLInsertStatement x) {
        handler.handleSQLInsertStatement(x);
        return super.visit(x);
    }

    @Override
    public boolean visit(SQLDeleteStatement x) {
        handler.handleSQLDeleteStatement(x);
        return super.visit(x);
    }

    @Override
    public boolean visit(SQLUpdateStatement x) {
        handler.handleSQLUpdateStatement(x);
        return super.visit(x);
    }

    @Override
    public boolean visit(SQLSelectQueryBlock x) {
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
