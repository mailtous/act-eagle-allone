package com.artlongs.framework.utils;

import org.osgl.logging.L;
import org.osgl.logging.Logger;
import org.osgl.util.C;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * @Func : 类属性COPY TOOLS
 * @Autor: leeton
 * @Date : 2018-04-24 4:18 PM
 */
public class BeanUtils {
    private static final Logger logger = L.get(BeanUtils.class);

    public enum RW {
        READ, WRITE;
    }

    public static <T> T copyTo(Object source, T target, String... ignList) {
        copy(source, target, ignList);
        return (T) target;
    }

    /**
     * COPY 属性
     *
     * @param source  原类
     * @param target  目标类
     * @param cache   是否要启用缓存
     * @param ignList 忽略列表
     */
    public static void copy(Object source, Object target, String... ignList) {
        if (null == source || target == null) {//为空,则不进行COPY属性
            return;
        }
        Set<Field> trageFieldList = getFields(target.getClass(), new HashSet<>());
        Set<Method> targetMethodList = getMethodsInFields(target.getClass(), trageFieldList, RW.WRITE);
        Set<Field> sourceFieldList = getFields(source.getClass(), new HashSet<>());
        Set<Method> sourceMethodList = getMethodsInFields(source.getClass(), sourceFieldList, RW.READ);
        if (targetMethodList.isEmpty() || sourceMethodList.isEmpty()) {
            logger.error("targetMethodList OR sourceMethodList is EMPTY !");
            return;
        }
        if (C.notEmpty(sourceMethodList)) {
            for (Method sMethod : sourceMethodList) {
                try {
                    Object value = sMethod.invoke(source);
                    if (null != value) {
                        Method writeMethod = getMethodByName(targetMethodList, sMethod.getName());
                        if (null != writeMethod) {
                            writeMethod.invoke(target, value);
                        }
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Set<Method> getMethodsInFields(Class clz, Set<Field> fields, RW rw) {
        Set<Method> allMmethod = getAllMethod(clz, new HashSet<>());
        Set<Method> methodList = (RW.READ == rw) ? readMethods(allMmethod) : writeMethods(allMmethod);
        Set<Method> tempList = new HashSet<>();
        for (Method method : methodList) {
            if (findOfField(fields, method.getName())) {
                tempList.add(method);
            }
        }
        return tempList;
    }

    private static Method getMethodByName(Set<Method> methodList, String name) {
        for (Method method : methodList) {
            name = replacePrefix(name);
            if (name.equalsIgnoreCase(replacePrefix(method.getName()))) {
                return method;
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

    private static Set<Field> getFields(Class clz, Set<Field> fieldList) {
        getFieldsIter(clz, fieldList);
        return fieldList;
    }

    /**
     * 遍历取得类的私有属性（包含父类属性）
     *
     * @param clz
     * @param fieldCache
     */
    private static void getFieldsIter(Class clz, Set<Field> fieldCache) {
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isFinal(field.getModifiers())) continue;
            if (Modifier.isStatic(field.getModifiers())) continue;
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
    private static Set<Method> getAllMethod(Class clz, Set<Method> cache) {
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


    private static Set<Method> readMethods(Set<Method> allMethod) {
        Set<Method> methodList = new HashSet<>();
        for (Method method : allMethod) {
            if (!isSkipMethod(method)) {
                if (method.getName().startsWith("get") || method.getName().startsWith("is")) {
                    methodList.add(method);
                }
            }
        }
        return methodList;
    }

    private static Set<Method> writeMethods(Set<Method> allMethod) {
        Set<Method> methodList = new HashSet<>();
        for (Method method : allMethod) {
            if (!isSkipMethod(method)) {
                if (method.getName().startsWith("set")) {
                    methodList.add(method);
                }
            }
        }
        return methodList;
    }

    private static boolean isTransientAnno(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if ("transient".equalsIgnoreCase(annotation.annotationType().getSimpleName())) return true;
        }
        return false;
    }

    private static boolean findOfField(Set<Field> fieldCache, String methodName) {
        for (Field field : fieldCache) {
            if (Modifier.isFinal(field.getModifiers())) continue;
            if (new String("get" + field.getName()).equalsIgnoreCase(methodName)) {
                return true;
            }
            if (new String("is" + field.getName()).equalsIgnoreCase(methodName)) {
                return true;
            }
            if (new String("set" + field.getName()).equalsIgnoreCase(methodName)) {
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {

        class Foo{
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

        foo.setId(111);
        foo.setName("alice");
        BeanUtils.copy(foo, foo2);
        System.err.println("foo2= " + foo2.toString());


    }

}
