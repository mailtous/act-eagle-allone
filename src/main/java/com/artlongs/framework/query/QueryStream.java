package com.artlongs.framework.query;


/**
 * Created by ${leeton} on 2018/7/24.
 */
public class QueryStream<T> extends LazyWrappedStream<T> implements QA<T> {
    public QueryStream() {
    }

    public QueryStream(QA<T> wrapped) {
        super(wrapped);
    }

    protected QueryStream wrap(QA<T> mapper) {
        return new QueryStream<>(mapper);
    }

    @Override
    public <R> QA<R> select(T t ,Select<T,R> s) {
        R r = s.apply(t);

       return  null;
    }


}
