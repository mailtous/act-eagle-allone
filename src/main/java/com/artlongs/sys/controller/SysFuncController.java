package com.artlongs.sys.controller;

import act.controller.annotation.TemplateContext;
import act.controller.annotation.UrlContext;
import act.view.RenderAny;
import com.artlongs.framework.page.Page;
import com.artlongs.framework.vo.BizRetVo;
import com.artlongs.sys.model.SysFunc;
import com.artlongs.sys.service.SysFuncService;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.annotation.PostAction;
import org.osgl.mvc.result.RenderJSON;

import javax.inject.Inject;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 11/30/17
 */
@UrlContext("/sys/func")
@TemplateContext("/sys/func")
public class SysFuncController extends SysBaseController {

    @Inject
    private SysFuncService sysFuncService;

    @GetAction("list")
    public RenderAny list(Integer pageNo) {
        Page<SysFunc> page = new Page<>().setPageNo(pageNo);
        String sql = " select * from sys_func";
        page = sysFuncService.getPage(page, sql, null);
        return render("list.html",page);
    }

    @PostAction("add")
    public RenderJSON add(SysFunc sysFunc) {
        sysFuncService.add(sysFunc);
        return json(new BizRetVo<>().setSuccess("系统功能添加成功!"));
    }

    @PostAction("edit/{id}")
    public RenderJSON edit(SysFunc sysFunc) {
        sysFuncService.update(sysFunc);
        return json(new BizRetVo<>().setSuccess("用户资料编辑成功!"));
    }

    @PostAction("del/{id}")
    public RenderJSON del(Long funcId) {
        SysFunc sysFunc = sysFuncService.get(funcId);
        if (null != sysFunc) {
            sysFuncService.realDelFuncAndPermisson(funcId);
        }
        return json(new BizRetVo<>().setSuccess("功能项删除成功!"));
    }

    @PostAction("on/{id}")
    public RenderJSON on(Long funcId) {
        return json(onOff(funcId,SysFunc.ON));
    }

    @PostAction("off/{id}")
    public RenderJSON off(Long funcId) {
        return json(onOff(funcId,SysFunc.OFF));
    }

    private BizRetVo onOff(Long funcId, int onOff) {
        SysFunc sysFunc = sysFuncService.get(funcId);
        if (null != sysFunc) {
            sysFunc.setAction(onOff);
            return new BizRetVo<>().setSuccess("功能状态已变更!");
        }
        return new BizRetVo<>().setError("找不到对应的功能或菜单!");
    }

}
