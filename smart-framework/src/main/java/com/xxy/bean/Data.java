package com.xxy.bean;

/**
 * 返回数据对象
 * 返回的Data类型的数据对象封装了一个Object类型的模型数据
 */
public class Data {
    /**
     * 模型数据
     */
    private Object model;

    public Data(Object model) {
        this.model = model;
    }

    public Object getModel() {
        return model;
    }
}
