package com.artlongs.framework.utils;

import org.osgl.$;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 利用 JVM 类重名机制 cracker 并解析成当前类
 * Created by lee on 2018/6/27.
 */
public class SerializedLambda implements Serializable{

    private static final long serialVersionUID = 8025925345765570181L;
    public final Object[] capturedArgs = null;
    public final String implClass = null;
    public final String implMethodName = null;
    public final String implMethodSignature = null;

    public String instantiatedMethodType;
    public Class<?> capturingClass;
    public String functionalInterfaceClass;
    public String functionalInterfaceMethodName;
    public String functionalInterfaceMethodSignature;
    public int implMethodKind;

    public String lambdaClassName;
    public String lambdaMethodName;
    public Class<?> lambdaClass;

    private static final Map<Object,SerializedLambda> cache = new ConcurrentHashMap<Object,SerializedLambda>();


    public static SerializedLambda parseLambda(Object lambda) {
        SerializedLambda extracted = cache.get(lambda);
        if (null != extracted) {
            return extracted;
        }
        extracted = SerializedLambda.extractLambda((Serializable) lambda);
        if (null != extracted) {
            extracted.lambdaClassName = parseClassName(extracted.instantiatedMethodType);
            extracted.lambdaMethodName = parseMethodName(extracted.implMethodName);
            extracted.lambdaClass = parseClassOfMethodType(extracted.instantiatedMethodType);
            cache.put(lambda, extracted);
        }
        return extracted;

    }

    /**
     * 利用 JVM 类重名机制 cracker 并解析成当前类
     * @param lambda
     * @return
     */
    private static SerializedLambda extractLambda(Serializable lambda) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream(2000);
            try (ObjectOutputStream out = new ObjectOutputStream(byteOut)) {
                out.writeObject(lambda);
            }

            byte[] data = byteOut.toByteArray();
            try (ObjectInputStream in = new ObjectInputStream(
                    new ByteArrayInputStream(data)) {

                @Override
                protected Class<?> resolveClass(ObjectStreamClass desc)
                        throws IOException, ClassNotFoundException {

                    Class<?> resolvedClass = super.resolveClass(desc);
                    if (resolvedClass == java.lang.invoke.SerializedLambda.class) return SerializedLambda.class;
                    return resolvedClass;
                }

            }) {
                return (SerializedLambda) in.readObject();
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



    private static String parseClassName(String methodType) {
        String[] pos = methodType.split(";");
        String className = pos[0].substring(pos[0].lastIndexOf("/")+1);
        return className;
    }

    private static String parseMethodName(String implMethodName) {
        return implMethodName.startsWith("get")?implMethodName.substring(3):implMethodName.substring(2);
    }

    private static Class<?> parseClassOfMethodType(String methodType) {
        String[] pos = methodType.split(";");
        String className = pos[0].replace("(L", "").replace("/",".");
        Class<?> clz = null;
        try {
            clz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.err.println(" class paser on error of" + methodType);
        }
        return clz;
    }

}
