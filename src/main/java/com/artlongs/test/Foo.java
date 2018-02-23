package com.artlongs.test;

import java.util.ArrayList;
import java.util.List;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 2/5/18
 */
public class Foo<T> {
    private List<T> items = new ArrayList<T>();  //返回数据列表

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
