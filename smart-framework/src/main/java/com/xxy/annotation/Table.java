package com.xxy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//该注解可以在程序运行期间被获取到
@Target(ElementType.TYPE)//该注解表示只能用于类
public @interface Table {
    String value() default "";
}
