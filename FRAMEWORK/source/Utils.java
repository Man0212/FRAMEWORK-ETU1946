package etu1946.framework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.reflect.Field;


public class Utils {
    
     public static Object castValueWithType(Class<?> parameterType, String value) {
            if (parameterType == int.class || parameterType == Integer.class) {
                return Integer.parseInt(value);
            } else if (parameterType == double.class || parameterType == Double.class) {
                return Double.parseDouble(value);
            } else if (parameterType == float.class || parameterType == Float.class) {
                return Float.parseFloat(value);
            } else if (parameterType == long.class || parameterType == Long.class) {
                return Long.parseLong(value);
            } else if (parameterType == boolean.class || parameterType == Boolean.class) {
                return Boolean.parseBoolean(value);
            } else if (parameterType == String.class) {
                return value;
            } else if (parameterType == Date.class) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    return dateFormat.parse(value);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        }

        public static void resetFieldsToDefaultValues(Object obj) throws IllegalAccessException {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                Object defaultValue = getDefaultValue(fieldType);
                field.set(obj, defaultValue);
            }
        }

        public static Object getDefaultValue(Class<?> fieldType) {
            if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
                return 0;
            } else if (fieldType.equals(double.class) || fieldType.equals(Double.class)) {
                return 0.0;
            } else if (fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) {
                return false;
            } else if (fieldType.equals(String.class)) {
                return "";
            } else if (fieldType.equals(Date.class)) {
                return new Date();
            }
            return null;
        }


}
