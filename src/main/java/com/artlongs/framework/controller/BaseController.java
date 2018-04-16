package com.artlongs.framework.controller;

import act.app.ActionContext;
import act.controller.Controller;
import act.event.EventBus;
import org.osgl.logging.L;
import org.osgl.logging.Logger;

import javax.inject.Inject;

/**
 * Controller 基类
 * Created by leeton on 9/25/17.
 */
public class BaseController extends Controller.Util{

    protected Logger logger = L.get(this.getClass());

    @Inject
    protected ActionContext ctx;
    @Inject
    protected EventBus eventBus;


    /**
     * 统一跳转页面
     */
    public void to(String url){
        throw Controller.Util.redirect(url);
    }
}
