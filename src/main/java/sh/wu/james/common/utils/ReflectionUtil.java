package sh.wu.james.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ReflectionUtils;

/**
 * @author James Wu
 * 
 */
public class ReflectionUtil {

    public static Object getValue(Object obj, String fieldName) {
        Field field = ReflectionUtils.findField(obj.getClass(), fieldName);
        if (field == null) {
            return null;
        }
        ReflectionUtils.makeAccessible(field);
        return ReflectionUtils.getField(field, obj);
    }

    public static void setValue(Object obj, String fieldName, Object value) {
        Field field = ReflectionUtils.findField(obj.getClass(), fieldName);
        if (field != null) {
            if (!Modifier.isFinal(field.getModifiers())) {
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, obj, value);
            }
        }
    }

    private static Field getField(Object obj, String fieldName) {
        return ReflectionUtils.findField(obj.getClass(), fieldName);
    }

    public static Object invoke(Object obj, String name) {
        Method method = ReflectionUtils.findMethod(obj.getClass(), name);
        ReflectionUtils.makeAccessible(method);
        return ReflectionUtils.invokeMethod(method, obj);
    }

    public static <T> T resetNullValue(T obj) {
        return resetNullValue(obj, null, null);
    }

    public static <T> T resetNullValueExceptFieldNames(T obj, String... excludeFields) {
        return resetNullValue(obj, excludeFields, null);
    }

    public static <T> T resetNullValueExceptFieldTypes(T obj, Class... excludeTypes) {
        return resetNullValue(obj, null, excludeTypes);
    }

    public static <T> T resetNullValue(T obj, String[] excludeFields, Class[] classes) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (ArrayUtils.contains(excludeFields, fieldName)) {
                continue;
            }
            if (ArrayUtils.contains(classes, field.getType())) {
                continue;
            }
            if (null == getValue(obj, fieldName)) {
                Object defaultValue = null;
                if (field.getType() == Double.class) {
                    defaultValue = Double.valueOf(0);
                }
                if (field.getType() == Boolean.class) {
                    defaultValue = false;
                } else if (field.getType() == Float.class) {
                    defaultValue = Float.valueOf(0);
                } else if (field.getType() == BigDecimal.class) {
                    defaultValue = new BigDecimal(0);
                } else if (field.getType() == Integer.class) {
                    defaultValue = Integer.valueOf(0);
                } else if (field.getType() == Long.class) {
                    defaultValue = Long.valueOf(0);
                } else if (field.getType() == String.class) {
                    defaultValue = "";
                } else if (field.getType() == Date.class) {
                    defaultValue = new Date(0);
                } else if (field.getType().isEnum()) {
                    defaultValue = field.getType().getEnumConstants()[0];
                }
                setValue(obj, fieldName, defaultValue);
            }
        }
        return obj;
    }

    public static String[] hasFieldsEmpty(Object obj, String... fieldNameList) {
        Field[] fields = obj.getClass().getDeclaredFields();
        List<String> resultFieldNames = new ArrayList<String>();
        for (Field field : fields) {
            if (ArrayUtils.contains(fieldNameList, field.getName())) {
                if (null == getValue(obj, field.getName())) {
                    resultFieldNames.add(field.getName());
                }
            }
        }
        return resultFieldNames.toArray(new String[0]);
    }

    public static <T> T setDefaultValue(T obj, Class<String> classType, Object defaultValue) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().equals(classType)) {
                setValue(obj, field.getName(), defaultValue);
            }
        }
        return obj;
    }

    public static void copyPropertiesWithNull(Object dest, Object src) throws SecurityException, NoSuchFieldException {
        copyProperties(dest, src, true);
    }

    public static void copyPropertiesWithNotNull(Object dest, Object src) throws SecurityException,
            NoSuchFieldException {
        copyProperties(dest, src, false);
    }

    private static void copyProperties(Object dest, Object src, Boolean withNullValue) throws SecurityException,
            NoSuchFieldException {
        String[] destFieldNames = getFieldNames(src);
        for (Field field : dest.getClass().getDeclaredFields()) {
            try {
                String fieldName = field.getName();
                if (ArrayUtils.contains(destFieldNames, fieldName)) {
                    Object srcValue = getValue(src, field.getName());
                    if (srcValue != null || withNullValue) {
                        setValue(dest, field.getName(), srcValue);
                    }
                }
            } catch (Exception e) {
                Logger.error(ReflectionUtil.class, "failed to copy property:" + field.getName(), e);
            }
        }

    }

    private static String[] getFieldNames(Object src) {
        List<String> fieldNameList = new ArrayList<String>();
        for (Field field : src.getClass().getDeclaredFields()) {
            fieldNameList.add(field.getName());
        }
        return fieldNameList.toArray(new String[] {});
    }

    public static void copyProptertiesFromMapWithBlackList(Object obj, Map<String, String> reqProps, String[] blackList) {
        for (String key : reqProps.keySet()) {
            if (ArrayUtils.contains(blackList, key)) {
                continue;
            }
            String fromVal = reqProps.get(key);
            if (StringUtils.isEmpty(fromVal)) {
                setValue(obj, key, null);
            } else {
                Field field = getField(obj, key);
                Object toVal = null;
                if (field.getType() == Double.class) {
                    toVal = Double.valueOf(0);
                }
                if (field.getType() == Boolean.class) {
                    toVal = Boolean.valueOf(fromVal);
                } else if (field.getType() == Float.class) {
                    toVal = Float.valueOf(fromVal);
                } else if (field.getType() == BigDecimal.class) {
                    toVal = new BigDecimal(fromVal);
                } else if (field.getType() == Integer.class) {
                    toVal = Integer.valueOf(fromVal);
                } else if (field.getType() == Long.class) {
                    toVal = Long.valueOf(fromVal);
                } else if (field.getType() == String.class) {
                    toVal = fromVal;
                } else if (field.getType() == Date.class) {
                    try {
                        toVal = new SimpleDateFormat("yyyy-MM-dd").parse(fromVal);
                    } catch (ParseException e) {
                        throw new IllegalArgumentException(fromVal + " cannot be convert to a date");
                    }
                } else if (field.getType().isEnum()) {
                    Object[] enumConstants = field.getType().getEnumConstants();
                    for (int i = 0; i < enumConstants.length; i++) {
                        if (enumConstants[i].toString().equals(fromVal)) {
                            toVal = enumConstants[i];
                        }
                    }
                }
                setValue(obj, key, toVal);
            }

        }

    }

}
