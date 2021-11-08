package com.coolxiaoyao.mybatislog.druid;

/**
 * @author kerryzhang on 2021/10/27
 */


class MysqlASTVisitor extends ASTVisitor {

    MysqlASTVisitor(MysqlASTHandler handler) {
        super(handler);
    }
}
