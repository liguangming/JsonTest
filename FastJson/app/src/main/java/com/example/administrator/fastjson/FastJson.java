package com.example.administrator.fastjson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/4/8.
 */

public class FastJson {

    public static <T> T parseObject(String json, Class<T> clazz) {
        Object object = toObject(json, clazz);
        return (T) object;
    }

    private static <T> Object toObject(String json, Class clazz) {
        Object object = null;
        //拿到model的成员变量 集合 目的 映射
        List<Field> list = getAllFields(clazz);
        //jsonObject 的处理
        if (json.charAt(0) == '{') {
            try {
                JSONObject jsonObject = new JSONObject(json);
                object = clazz.newInstance();
                Iterator iterator = jsonObject.keys();

                while (iterator.hasNext()) {
                    //得到的json key
                    String key = (String) iterator.next();

                    for (Field field : list) {
                        if (field.getName().equals(key)) {
                            //进行赋值
                            field.setAccessible(true);
                            Object fieValue = getFieldValue(field, jsonObject, key);
                            field.set(object, fieValue);
                        }

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        } else if (json.charAt(0) == '[') {
            object=toList(json,clazz);
        }
        return object;
    }

    /**
     * 处理jsonArray
     * */
    private static List toList(String json, Class clazz) {
        List<Object> list=null;
        try {
            JSONArray jsonArray=new JSONArray(json);
            list=new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                String jsonValue=jsonArray.getJSONObject(i).toString();
                switch (getJSONType(jsonValue)){
                    case JSON_TYPE_ARRAY:
                        //JOSNARRAY 数组，还有array数组的情况
                        List infoList=toList(jsonValue,clazz);
                        list.add(infoList);
                        break;
                    case JSON_TYPE_OBJECT:
                        list.add(toObject(jsonValue,clazz));
                        break;
                    case JSON_TYPE_ERROR:
                        break;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;

    }

    /**
     * 获取key对应的值
     */

    private static Object getFieldValue(Field field, JSONObject jsonObject, String key) throws JSONException {
        Object fieldValue = null;
        //拿到Class类型
        Class fieldClass = field.getType();
        if (fieldClass.getSimpleName().equals("int") || fieldClass.getSimpleName().equals("Integer")) {
            fieldValue = jsonObject.getInt(key);
        } else if (fieldClass.getSimpleName().equals("String")) {
            fieldValue = jsonObject.getString(key);
        } else if (fieldClass.getSimpleName().equals("Date")) {
//            fieldValue = jsonObject.getDate(key);
        } else if (fieldClass.getSimpleName().equals("double") || fieldClass.getSimpleName().equals("Double")) {
            fieldValue = jsonObject.getDouble(key);
        } else if (fieldClass.getSimpleName().equals("Long") || fieldClass.getSimpleName().equals("long")) {
            fieldValue = jsonObject.getLong(key);
        }else{
            //类类型
            String jsonValue=  jsonObject.getString(key);
            switch (getJSONType(jsonValue)){
                case JSON_TYPE_ARRAY:
                    Type gennicFieldType=field.getGenericType();
                    //拿到list集合所对应的泛型
                    if(gennicFieldType instanceof ParameterizedType){
                        //list ---Friend
                        ParameterizedType parameterizedType= (ParameterizedType) gennicFieldType;
                        Type[] fieldTypes=parameterizedType.getActualTypeArguments();
                        for(Type really:fieldTypes){
                            Class <?> fieldClazz=(Class<?>) really;
                            fieldValue=toList(jsonValue,fieldClazz);
                        }

                    }
                    break;
                case JSON_TYPE_OBJECT:
                    toObject(jsonValue,fieldClass);
                    break;
                default:
                    break;
            }
        }
        return fieldValue;
    }
    /**
     * 获取json类型
     */
    private static JSON_TYPE getJSONType(String jsonValue) {

        final char[] strChar=jsonValue.substring(0,1).toCharArray();
        final char firstChart = strChar[0];
        if(firstChart=='{'){
            return JSON_TYPE.JSON_TYPE_OBJECT;
        }else if(firstChart=='['){
            return JSON_TYPE.JSON_TYPE_ARRAY;
        }else {
            return JSON_TYPE.JSON_TYPE_ERROR;
        }
    }

    private enum JSON_TYPE{
        JSON_TYPE_ARRAY,
        JSON_TYPE_OBJECT,
        JSON_TYPE_ERROR
    }
    /**
     * 获取属性
     */
    private static List<Field> getAllFields(Class<?> clazz) {
        ArrayList<Field> list = new ArrayList<>();
        //应该拿到父类的成员变量
        while ((clazz != null)) {
            String name = clazz.getName();
            if (name.startsWith("java.") || name.startsWith("javax") || name.startsWith("android")) {
                break;
            }
            Field[] fieldSelf = clazz.getDeclaredFields();
            for (Field field : fieldSelf) {
                if (!Modifier.isFinal(field.getModifiers())) {
                    list.add(field);
                }

            }
            return list;
        }
       return null;
    }

}
