package com.artlongs.test;

import com.artlongs.framework.page.Page;
import org.osgl.util.C;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by leeton on 10/12/17.
 */
public class Test{
    public static void main(String[] args) throws Exception {
//        Act.start("act-eagle");

        String sf = " username,userId";
        String ss = "DESC";
        String s = megerSfSc(sf, ss);

        System.err.println("s= " + s);


        orderConditionSplit(s, new Page());


    }

    public static String megerSfSc(String sf,String sc){
        if (null == sf || sf.trim().equals("")) return "";
        if(null == sc || sc.trim().equals("")) sc = "asc";
        String[] sfs = sf.split(",");
        String[] scs = sc.split(",");
        List<String> scList = new ArrayList<>();
        if (scs.length != sfs.length) { //排序的方向,与排序的字段个数不匹配,默认追加 "asc"
            List<String> tempList =Arrays.asList(scs);
            scList.addAll(tempList);
            for (int i = 0; i < sfs.length - scs.length; i++) {
                scList.add("asc");
            }
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < sfs.length; i++) {
            String s = String.format("%s %s ", sfs[i], scList.get(i));
            sb.append(s);
            if(i < sfs.length-1) sb.append(", ");
        }

        return sb.toString();

    }

    private static void orderConditionSplit(String orderBy,Page page){
        orderBy = orderBy.trim().toLowerCase();
        String[] orderArr = orderBy.split(",");
        StringBuffer sfb = new StringBuffer();
        StringBuffer scb = new StringBuffer();
        for (int i = 0; i < orderArr.length; i++) {
            sfb.append(",");
            sfb.append(orderArr[i].trim().split(" ")[0]);
            scb.append(",");
            scb.append(orderArr[i].trim().split(" ")[1]);
        }
        sfb.delete(0, 1);
        scb.delete(0,1);

    }


}
