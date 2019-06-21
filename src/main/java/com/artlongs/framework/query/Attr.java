package com.artlongs.framework.query;

import com.trigersoft.jaque.expression.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.lang.reflect.Member;
import java.util.List;
import java.util.function.Function;

/**
 * @Func : 分析 lambda 方法
 * @Autor: leeton
 * @Date : 2018-03-27 6:36 PM
 */
public class Attr<T> {

    private Class clz;     // 方法所属的类
    private String name;   // 属性名
    private String column; // 对应的字段名称
    private Member member;
    private String tableName;

    // this interface is required to make the lambda Serializable, which removes a need for
    // jdk.internal.lambda.dumpProxyClasses system property. See below.
    public interface Property<T, R> extends Function<T, R>, Serializable { }

    public Attr() {
    }

    public Attr(Property<T, ?> propertyRef) {
        parse(propertyRef);
    }

    /**
     *
     * 传入 lambda 静态方法，返回方法属性名及对应的字段名
     * @param propertyRef
     * @return
     */
    public Attr<T> parse(Property<T, ?> propertyRef){
        LambdaExpression expression = getExpression(propertyRef);
        List<ParameterExpression> params = expression.getParameters();
        this.member = getMember(expression);
        this.clz = params.get(0).getResultType();
        this.name = attrName(member);
        this.column = getUnderLineName(this.name);
        this.tableName = getRealTableName(this.clz);
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
        return getCamelName(member.getName());
    }

    private String getCamelName(String method) {
        method = method.startsWith("get") ? method.substring(3) : method.substring(2);
        return StringKit.toLowerCaseFirstOne(method);
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
        Member member = getMember(getExpression(fun));
        return getCamelName(member.getName());
    }

    public String[] attrName(Property<T, ?>... funs) {
        String[] cols = new String[funs.length];
        int i = 0;
        for (Property<T, ?> fun : funs) {
            cols[i++] = attrName(fun);
        }
        return cols;
    }

    public static String getRealTableName(Class<?> c) {
        Table table = (Table) c.getAnnotation(Table.class);
        Entity entity = (Entity) c.getAnnotation(Entity.class);
        if (table != null && !table.name().isEmpty()) {
            return table.name();
        } else if (entity != null && !entity.name().isEmpty()) {
            return entity.name();
        }
        return StringKit.enCodeUnderlined(c.getSimpleName());
    }


    public String getUnderLineName(String arrtName) {
        return StringKit.enCodeUnderlined(arrtName);
    }


    public static String[] getColumns(Attr... methods) {
        String[] cols = new String[methods.length];
        int i = 0;
        for (Attr method : methods) {
            cols[i++] = method.getColumn();
        }
        return cols;

    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Class getClz() {
        return clz;
    }

    public void setClz(Class clz) {
        this.clz = clz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
}
