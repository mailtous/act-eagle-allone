package com.artlongs.framework.utils;

import com.trigersoft.jaque.expression.*;
import org.beetl.sql.core.UnderlinedNameConversion;

import java.io.Serializable;
import java.lang.reflect.Member;
import java.util.List;
import java.util.function.Function;

/**
 * @Func : 分析 lambda 方法
 * @Autor: leeton
 * @Date : 2018-03-27 6:36 PM
 */
public class Method<T> {

    private Class clz;     // 方法所属的类
    private String attr;   // 属性名
    private String column; // 对应的字段名称
    private Member member;  

    // this interface is required to make the lambda Serializable, which removes a need for
    // jdk.internal.lambda.dumpProxyClasses system property. See below.
    public interface Property<T, R> extends Function<T, R>, Serializable { }

    public Method() {
    }

    public Method(Property<T, ?> propertyRef) {
        parse(propertyRef);
    }

    /**
     *
     * 传入 lambda 静态方法，返回方法属性名及对应的字段名
     * @param propertyRef
     * @return
     */
    public Method<T> parse(Property<T, ?> propertyRef){
        LambdaExpression expression = getExpression(propertyRef);
        List<ParameterExpression> params = expression.getParameters();
        this.member = getMember(expression);
        this.clz = params.get(0).getResultType();
        this.attr = attrName(member);
        this.column = getUnderLineName(this.clz,  this.attr);
        return this;
    }

    public LambdaExpression getExpression(Property<T, ?> propertyRef) {
        return LambdaExpression.parse(propertyRef);
    }
    public static Member getMember(LambdaExpression lambdaExpression) {
        Expression methodCall = lambdaExpression.getBody();
        // remove casts
        while (methodCall instanceof UnaryExpression) {
            methodCall = ((UnaryExpression) methodCall).getFirst();
        }

        // checks are omitted for brevity
        Member member = ((MemberExpression) ((InvocationExpression) methodCall).getTarget()).getMember();

        return member;
    }

    public String attrName(Member member) {
        String method = member.getName();
        return method.startsWith("get")?method.substring(3):method.substring(2);
    }

    public String[] attrName(Member... member) {
        String[] cols = new String[member.length];
        int i = 0;
        for (Member fun : member) {
            cols[i++] = attrName(fun);
        }
        return cols;
    }

    public String attrName(Property<T, ?> fun) {
        LambdaExpression<Function<T, ?>> lambdaExpression = getExpression(fun);
        Member member = getMember(lambdaExpression);
        String method = member.getName();
        return method.startsWith("get")?method.substring(3):method.substring(2);
    }

    public String[] attrName(Property<T, ?>... funs) {
        String[] cols = new String[funs.length];
        int i = 0;
        for (Property<T, ?> fun : funs) {
            cols[i++] = attrName(fun);
        }
        return cols;
    }


    public String getUnderLineName(Class clz,String arrtName) {
        return new UnderlinedNameConversion().getColName(clz, arrtName);
    }


    public static String[] getColumns(Method... methods) {
        String[] cols = new String[methods.length];
        int i = 0;
        for (Method method : methods) {
            cols[i++] = method.getColumn();
        }
        return cols;

    }

    public Class getClz() {
        return clz;
    }

    public void setClz(Class clz) {
        this.clz = clz;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
}
