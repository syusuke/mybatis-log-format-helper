package com.coolxiaoyao.mybatislog.druid;

import java.util.List;

/**
 * @author kerryzhang on 2021/11/05
 */


public interface IVisitorResultRef<T> {

    /**
     * get refs
     *
     * @return
     */
    List<T> getRefs();

}
