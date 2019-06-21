package com.artlongs.framework.query;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Func :
 *
 * @author: leeton on 2019/6/14.
 */
public class BeanMapUtils {

    public static <T> T copyTo(Object source, T target, String... ignList) {
        copy(source, target, ignList);
        return (T) target;
    }

    /**
     * COPY 属性(对象与MAP通用)
     *
     * @param source  原类
     * @param target  目标类
     * @param ignList 忽略列表
     */
    public static void copy(Object source, Object target, String... ignList) {
        if (null == source || target == null) {//为空,则不进行COPY属性
            return;
        }
        if (target instanceof Map) {
            toMap(source, (Map<String, Object>) target, ignList);
            return;
        }
        if (source instanceof Map) {
            fromMap((Map<String, Object>) source, target, ignList);
            return;
        }
        toPojo(source, target, ignList);
    }

    private static void toPojo(Object source, Object target, String[] ignList) {
        Set<Field> trageFieldList = getFields(target.getClass(), new HashSet<>());
        Set<Field> sourceFieldList = getFields(source.getClass(), new HashSet<>());
        if (sourceFieldList.isEmpty() || trageFieldList.isEmpty()) {
            throw new RuntimeException("trageFieldList OR sourceFieldList is EMPTY !");
        }
        if (sourceFieldList.size() > 0) {
            for (Field sField : sourceFieldList) {
                if (isFilterAttr(Arrays.asList(ignList), sField.getName())) continue;
                Object value = getFieldValue(source, sField);
                if (null != value) {
                    Field field = getFieldByName(trageFieldList, sField.getName());
                    setFieldValue(target, field, value);
                }
            }
        }
    }

    private static void toMap(Object source, Map<String, Object> targetMap, String... ignList) {
        Set<Field> sourceFieldList = getFields(source.getClass(), new HashSet<>());
        if (sourceFieldList.isEmpty()) {
            throw new RuntimeException("trageFieldList is EMPTY !");
        }
        if (sourceFieldList.size() > 0) {
            for (Field sField : sourceFieldList) {
                if (isFilterAttr(Arrays.asList(ignList), sField.getName())) continue;
                Object value = getFieldValue(source, sField);
                if (null != value) {
                    targetMap.put(sField.getName(), value);
                }
            }
        }
    }

    private static void fromMap(Map<String, Object> sourceMap, Object target, String... ignList) {
        Set<Field> targetFields = getFields(target.getClass(), new HashSet<>());
        if (targetFields.isEmpty()) {
            throw new RuntimeException("trageFieldList is EMPTY !");
        }
        for (String key : sourceMap.keySet()) {
            if (isFilterAttr(Arrays.asList(ignList), key)) continue;
            Object val = sourceMap.get(key);
            Field field = getFieldByName(targetFields, key);
            setFieldValue(target, field, val);
        }
    }

    private static boolean isFilterAttr(List<String> filterList, String currentAttr) {
        for (String name : filterList) {
            if (name.equals(currentAttr)) return true;
        }
        return false;
    }


    private static Field getFieldByName(Set<Field> fields, String name) {
        for (Field field : fields) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    private static String replacePrefix(String name) {
        if (name.startsWith("get") || name.startsWith("set")) {
            name = name.substring(3);
        }
        if (name.startsWith("is")) {
            name = name.substring(2);
        }
        return name;
    }

    public static Set<Field> getFields(Class clz, Set<Field> fieldList) {
        getFieldsIter(clz, fieldList);
        return fieldList;
    }

    /**
     * 遍历取得类的属性（包含父类属性）
     *
     * @param clz
     * @param fieldCache
     */
    private static void getFieldsIter(Class clz, Set<Field> fieldCache) {
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isFinal(field.getModifiers())) continue;
            if (Modifier.isStatic(field.getModifiers())) continue;
            if (findIgnoreAnno(field)) continue;
            fieldCache.add(field);
        }
        if (null != clz.getSuperclass() && (!clz.getSuperclass().getName().equalsIgnoreCase("java.lang.Object"))) {
            getFieldsIter(clz.getSuperclass(), fieldCache);
        }
    }

    /**
     * 遍历取得类的所有方法
     *
     * @param clz
     * @param cache
     */
    public static Set<Method> getAllMethod(Class clz, Set<Method> cache) {
        Method[] methods = clz.getDeclaredMethods();
        for (Method method : methods) {
            if (Modifier.isFinal(method.getModifiers())) continue;
            if (Modifier.isStatic(method.getModifiers())) continue;
            cache.add(method);
        }
        if (null != clz.getSuperclass() && (!clz.getSuperclass().getName().equalsIgnoreCase("java.lang.Object"))) {
            getAllMethod(clz.getSuperclass(), cache);
        }

        return cache;
    }

    private static boolean findIgnoreAnno(Field field) {
        Annotation[] annoList = field.getAnnotations();
        for (Annotation annotation : annoList) {
            if ("JsonIgnore".equalsIgnoreCase(annotation.annotationType().getSimpleName())) return true;
            if ("JsonBackReference".equalsIgnoreCase(annotation.annotationType().getSimpleName())) return true;
            if ("transient".equalsIgnoreCase(annotation.annotationType().getSimpleName())) return true;
        }
        return false;
    }

    /**
     * 按规则过滤方法
     *
     * @param method
     * @return
     */
    private static boolean isSkipMethod(Method method) {
        if (Modifier.isStatic(method.getModifiers())) return true;
        if (Modifier.isFinal(method.getModifiers())) return true;
        if (method.getName().equalsIgnoreCase("toString")) return true;
        if (method.getName().equalsIgnoreCase("hashCode")) return true;
        return false;
    }

    private static boolean isTransientAnno(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if ("transient".equalsIgnoreCase(annotation.annotationType().getSimpleName())) return true;
        }
        return false;
    }

    /**
     * 对属性设值
     *
     * @param targetClz  目标类
     * @param field
     * @param fieldValue
     */
    public static void setFieldValue(Object targetClz, Field field, Object fieldValue) {
        try {
            field.setAccessible(true);
            field.set(targetClz, fieldValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 取得属性的值
     *
     * @param targetClz 目标类
     * @param field
     * @param <T>
     * @return
     */
    public static <T> T getFieldValue(Object targetClz, Field field) {
        try {
            field.setAccessible(true);
            return (T) field.get(targetClz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {

        class Foo {
            private Integer id;
            private String name;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return "Foo{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
            }
        }
        Foo foo = new Foo();
        Foo foo2 = new Foo();
        Foo foo3 = new Foo();
        Map<String, Object> tMap = new HashMap<>();

        foo.setId(111);
        foo.setName("alice");
        System.err.println("foo1= " + foo.toString());

        BeanMapUtils.copy(foo, foo2);
        System.err.println("foo1 ->foo2 = " + foo2.toString());
        //
        BeanMapUtils.copy(foo, tMap);
        System.err.println("foo -> tMap = " + tMap.toString());

        BeanMapUtils.copy(tMap, foo3);
        System.err.println("tMap -> foo = " + foo3.toString());
    }
}
