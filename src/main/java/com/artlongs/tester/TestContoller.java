package com.artlongs.tester;

import act.controller.annotation.UrlContext;
import com.artlongs.framework.dao.BeetlSqlDao;
import com.artlongs.framework.query.Lq;
import org.osgl.mvc.annotation.GetAction;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Func :
 *
 * @author: leeton on 2019/6/13.
 */
@UrlContext("/test")
public class TestContoller {


    @GetAction("lq/update")
    public String lqUpdate() {
        User user = new User();
        user.setMoney(new BigDecimal(88888.888));
        user.setCreateDate(new Date());

        Lq<User> lq = new Lq(User.class,new BeetlSqlDao<>().getSqlm());
        lq.andEq(User::getUserName,"bond junk").toUpdate(user);

        return "Ok";
    }

    @GetAction("hello")
    public String hello() {
        return "hello";
    }
}
