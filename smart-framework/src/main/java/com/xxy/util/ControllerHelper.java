package com.xxy.util;

import com.xxy.annotation.Action;
import com.xxy.bean.Handler;
import com.xxy.bean.Request;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 控制器助手类
 */
public class ControllerHelper {
    /**
     * 用于存放请求与处理器的映射关系
     */
    private static final Map<Request, Handler> ACTION_MAP = new HashMap<Request, Handler>();

    static {
        //获取所有的Controller类
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if(CollectionUtils.isNotEmpty(controllerClassSet)){
            //遍历这些Controller类
            for(Class<?> controllerClass : controllerClassSet){
                //获取Controller类中定义的方法
                Method[] methods = controllerClass.getDeclaredMethods();
                if(ArrayUtils.isNotEmpty(methods)){
                    //遍历Controller中定义的所有方法
                    for(Method method : methods){
                        //判断当前方法中是否带有Action注解
                        if(method.isAnnotationPresent(Action.class)){
                            //从注解中获取URL映射规则
                            Action action = method.getAnnotation(Action.class);
                            String mapping = action.value();
                            //验证URL映射规则
                            if(mapping.matches("\\w+:/\\w*")){
                                String[] array = mapping.split(":");
                                if(ArrayUtils.isNotEmpty(array) && array.length == 2){
                                    //获取请求方法与请求路径
                                    String requestMethod = array[0];
                                    String requestPath = array[1];
                                    Request request = new Request(requestMethod,requestPath);
                                    Handler handler = new Handler(controllerClass, method);
                                    ACTION_MAP.put(request,handler);
                                }
                            }
                        }

                    }
                }

            }
        }
    }
    /**
     * 获取Handler
     */
    public static Handler getHandler(String requestMethod, String requestPath){
        Request request = new Request(requestMethod, requestPath);
        return  ACTION_MAP.get(request);
    }
}
