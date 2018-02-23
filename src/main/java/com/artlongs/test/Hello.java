package com.artlongs.test;

import act.controller.annotation.TemplateContext;
import act.controller.annotation.UrlContext;
import act.view.RenderAny;
import com.artlongs.framework.controller.BaseController;
import com.artlongs.framework.page.Page;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.result.RenderJSON;

import javax.inject.Inject;
import java.sql.SQLException;

/**
 * Created by leeton on 9/8/17.
 */
@UrlContext("/") //URL路径,相当于controller("/")
@TemplateContext("/") //对应的模板路径
public class Hello extends BaseController {

    @Inject
    UserService userService;


    @GetAction("foo")
    public RenderAny foo(Foo foo) throws SQLException {
        String msg = " hello to leeton.";
        return render("hello.html",foo);
    }



    @GetAction("hello")
    public RenderAny hello() throws SQLException {
        String msg = " hello to leetonton.";
        return render("hello.html",msg);
    }

    @GetAction("user/add")
    public RenderJSON addUser() throws SQLException {
        userService.tx.start();
        User user = new User("alice", "123", 1);
        userService.save(user);
        // int i = 1/0; //测试异常 rollback
        userService.tx.commit();

        return renderJson("Ok");
    }

    @GetAction("user/{id}")
    public RenderJSON getHello(Long id) throws SQLException {
        User user = userService.get(id);
        return renderJson(user);
    }

    @GetAction("user/del/{id}")
    public RenderJSON delUser(Long id) throws SQLException {
        userService.delete(id);
        return renderJson("Ok");
    }

    @GetAction("user/page.json")
    public RenderJSON pageOfjson() throws SQLException {
        String sql = " select * from user where id > ?";
        Page<User> userPage = userService.getPage(new Page(), sql, new Object[]{0});
        return renderJson(userPage);
    }

/*
    @GetAction("user/page")
    public RenderAny page(Integer pageNumber) throws SQLException {
        String sql = " select * from user where id > ?";
        Page<User> page = new Page<>().setPageNumber(pageNumber);
        page = userService.getPage(page, sql, new Object[]{0});
        page.setUrl("page");
        render(page);
        return tpl("user_page.html");
    }
*/




}
