package com.xxy.bean;

import com.xxy.util.CastUtil;

import java.util.Map;

/**
 * 请求参数对象:
 * 在Param类中会有一系列的get方法，可通过参数名获取指定类型的参数值，也可以获取所有参数的Map结构。
 */
public class Param {
    private Map<String, Object> paramMap;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }
    /**
     * 根据参数名获取long参数值
     */
    public long getLong(String name){
        return CastUtil.castLong(paramMap.get(name));
    }

    /**
     * 获取所有字段的信息
     */
    public Map<String, Object> getMap(){
        return paramMap;
    }

}
