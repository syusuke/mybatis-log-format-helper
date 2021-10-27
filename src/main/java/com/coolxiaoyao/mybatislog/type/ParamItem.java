package com.coolxiaoyao.mybatislog.type;

/**
 * @author kerryzhang on 2021/10/27
 */


public class ParamItem {
    private final String simpleName;
    private final Class<?> cls;
    private final Object value;

    public ParamItem(String simpleName, Class<?> cls, Object value) {
        this.simpleName = simpleName;
        this.cls = cls;
        this.value = value;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public Class<?> getCls() {
        return cls;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ParamItem{" +
                "simpleName='" + simpleName + '\'' +
                ", cls=" + cls +
                ", value=" + value +
                '}';
    }
}
