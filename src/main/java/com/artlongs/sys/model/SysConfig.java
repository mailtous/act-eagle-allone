package com.artlongs.sys.model;

import act.inject.AutoBind;
import com.artlongs.framework.dao.BeetlSqlDao;
import com.artlongs.framework.model.BaseEntity;

import javax.inject.Singleton;

/**
 * Created by lee on 5/22/18.
 */
public class SysConfig extends BaseEntity {

    private String domain; //域名
    private String copyright; //版权字样
    private String addr;
    private String comfullname;
    private String comshotname;
    private String keywords;
    private String title;
    private String metadesc;
    private String logourl;

    @Singleton
    public static abstract class Dao<T extends SysConfig> extends BeetlSqlDao<SysConfig> {
    }

    // ============================ girl && beast ============================

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getComfullname() {
        return comfullname;
    }

    public void setComfullname(String comfullname) {
        this.comfullname = comfullname;
    }

    public String getComshotname() {
        return comshotname;
    }

    public void setComshotname(String comshotname) {
        this.comshotname = comshotname;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMetadesc() {
        return metadesc;
    }

    public void setMetadesc(String metadesc) {
        this.metadesc = metadesc;
    }

    public String getLogourl() {
        return logourl;
    }

    public void setLogourl(String logourl) {
        this.logourl = logourl;
    }
}
