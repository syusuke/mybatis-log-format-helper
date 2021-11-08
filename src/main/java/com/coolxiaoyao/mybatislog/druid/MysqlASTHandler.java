package com.coolxiaoyao.mybatislog.druid;

import com.coolxiaoyao.mybatislog.type.ParamItem;

import java.util.List;

/**
 * @author kerryzhang on 2021/10/27
 */


class MysqlASTHandler extends ASTHandler {

    public MysqlASTHandler(List<ParamItem> params) {
        super(params);
    }
}
