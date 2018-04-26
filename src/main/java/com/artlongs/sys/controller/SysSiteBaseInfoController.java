package com.artlongs.sys.controller;

import act.controller.annotation.TemplateContext;
import act.controller.annotation.UrlContext;
import com.artlongs.framework.vo.R;
import com.artlongs.sys.model.SiteBaseInfo;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.result.Result;

import javax.inject.Inject;
import javax.persistence.Transient;

/**
 * @Func :
 * @Autor: leeton
 * @Date : 2018-04-23 7:57 PM
 */
@UrlContext("/sys/site/")
@TemplateContext("/sys/site/")
public class SysSiteBaseInfoController extends SysBaseController{

    @Transient
    @Inject
    private SiteBaseInfo.Dao<SiteBaseInfo> dao;
    
    @GetAction("setting/edit_box/{id}")
    public Result editBox(Long id) {
        SiteBaseInfo site = dao.get(id);
        site = site == null?new SiteBaseInfo():site;
        return render("/sys/site/edit_box",site);
    }

    @GetAction("setting/add")
    public Result add(SiteBaseInfo site) {
        R r = new R();
        dao.add(site);
        return json(r);
    }
    @GetAction("setting/update/{id}")
    public Result update(SiteBaseInfo site) {
        R r = new R();
        dao.update(site);
        return json(r);
    }


}
