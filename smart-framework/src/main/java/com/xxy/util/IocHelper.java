package com.xxy.util;

import com.sun.deploy.util.ArrayUtil;
import com.xxy.annotation.Inject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 依赖注入助手类
 */
public class IocHelper {
    static {
        //获取所有的Bean类与Bean实例之间的映射关系
        Map<Class<?>,Object> beanMap = BeanHelper.getBeanMap();
        if(MapUtils.isNotEmpty(beanMap)){
            //遍历beanMap
            for(Map.Entry<Class<?>,Object> beanEntry : beanMap.entrySet()){
                //从BeanMap中获取Bean类与Bean实例
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                //获取Bean类定义的所有成员变量
                Field[] beanFields = beanClass.getDeclaredFields();
                if(ArrayUtils.isNotEmpty(beanFields)){
                    //遍历Bean Filed
                    for(Field beanField : beanFields){
                        //判断当前BeanField 是否带有Inject注解
                        if(beanField.isAnnotationPresent(Inject.class)){
                            //在benamap 中获取Bean Field对应的实例
                            Class<?> beanFieldClass = beanField.getType();
                            Object beanFieldInstance = beanMap.get(beanFieldClass);
                            if(beanFieldInstance != null){
                                //通过反射初始化BeanField的值
                                ReflectionUtil.setField(beanInstance,beanField,beanFieldClass);
                            }
                        }
                    }
                }
            }
        }
    }
}
