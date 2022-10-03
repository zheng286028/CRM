package com.zzl.crm.commons.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能描述 :获取目标对象的数据
 *
 * @author 郑子浪
 * @date 2022/10/04  0:31
 */
public class ClassUtils {

    /**
     * 获取该对象的成员变量
     * @param target 目标对象
     * @return 返回目标对象的字段
     */
    public static List<String> getFields(Class<?> target){
        List<String> fieldsList = new ArrayList<>();
        Field[] fields = target.getDeclaredFields();

        //放入集合
        for (Field field : fields) {
            fieldsList.add(field.getName());
        }
        return fieldsList;
    }

    /**
     * 获取该对象的值
     * @param data 目标对象
     * @return 返回该对象的Map集合
     * declaredField.get(data)：获取该对象的值，从第一个字段开始获取
     */
    public static <T> Map<String,Object> getFieldValues(T data) throws IllegalAccessException {
        Field[] declaredFields = data.getClass().getDeclaredFields();
        Map<String,Object> fieldValue = new HashMap<>(declaredFields.length);
        for (Field declaredField : declaredFields) {
            //允许访问私有变量的值
            declaredField.setAccessible(true);
            //获取成员变量的值，封装
            fieldValue.put(declaredField.getName(),declaredField.get(data));
            System.out.println(declaredField.get(data));
        }
        return fieldValue;
    }
}
