package com.artlongs.framework.utils;

import java.io.Serializable;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by ${leeton} on 2018/7/20.
 */
interface QA<T> extends Stream<T> {

    <R> QA<R> select(T t ,Select<T, R> select);

    interface Select<T, R> extends Function<T,R>{};



}
