package com.xxy.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 转型操作工具类
 */
public class CastUtil {

    public static String castString(Object object){
        return CastUtil.castString(object,"");
    }

    /**
     * 转型字符串（提供默认值）
     * @param object
     * @param s
     * @return
     */
    public static String castString(Object object, String defaultValue) {
        return object != null ? String.valueOf(object) : defaultValue;
    }

    /**
     * 转为double类型
     * @param object
     * @return
     */
    public static double castDouble(Object object){
        return CastUtil.castDouble(object,0);
    }

    /**
     * 转为double类型提供默认值
     * @param object
     * @param defaultValue
     * @return
     */
    public static double castDouble(Object object, int defaultValue) {
        double value = defaultValue;
        if(object != null){
            String strValue = castString(object);
            if(StringUtils.isNotEmpty(strValue)){
                try {
                    value = Double.parseDouble(strValue);
                }catch (NumberFormatException e){
                    value = defaultValue;
                }
            }
        }
        return value;
    }

    public static long castLong(Object obj){
        return CastUtil.castLong(obj, 0);
    }

    public static long castLong(Object obj, int defaultValue) {
        long longValue = defaultValue;
        if(obj != null){
            String strValue = castString(obj);
            if(StringUtils.isNotEmpty(strValue)){
                try {
                    longValue = Long.parseLong(strValue);
                }catch (NumberFormatException e){
                    longValue = defaultValue;
                }
            }
        }
        return longValue;
    }

    /**
     * 转为int类型
     * @param property
     * @return
     */
    public static int castInt(Object obj) {

        return CastUtil.castInt(obj, 0);
    }

    public static int castInt(Object obj, int defaultValue) {
        int intValue = defaultValue;
        if(obj != null){
            String strValue = castString(obj);
            if(StringUtils.isNotEmpty(strValue)){
                try {
                    intValue = Integer.parseInt(strValue);
                }catch (NumberFormatException e){
                    intValue = defaultValue;
                }
            }
        }
        return intValue;
    }

    public static boolean castBoolean(Object obj) {
        return CastUtil.castBoolean(obj, false);
    }

    public static boolean castBoolean(Object obj, boolean defaultValue) {
        boolean booleanValue = defaultValue;
        if(obj != null){
            String strValue = castString(obj);
            if(StringUtils.isNotEmpty(strValue)){
                try {
                    booleanValue = Boolean.parseBoolean(strValue);
                }catch (NumberFormatException e){
                    booleanValue = defaultValue;
                }
            }
        }
        return booleanValue;
    }
}
