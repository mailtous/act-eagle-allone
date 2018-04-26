package com.artlongs.sys.model;

import act.inject.AutoBind;
import com.artlongs.framework.dao.BeetlSqlDao;
import com.artlongs.framework.model.BaseEntity;

import javax.inject.Singleton;
import java.util.Date;

/**
 * @Func : 站点基础信息
 * @Autor: leeton
 * @Date : 2018-04-23 1:26 PM
 */
public class SiteBaseInfo  extends BaseEntity{
    private String siteName;
    private String siteIp;
    private String siteUrl;
    private String email;
    private String addr;
    private String comName;

    @Singleton
    @AutoBind
    public static abstract class Dao<T extends SiteBaseInfo> extends BeetlSqlDao<SiteBaseInfo> {

        public SiteBaseInfo add(SiteBaseInfo siteBaseInfo) {
            siteBaseInfo.addTime();
            super.save(siteBaseInfo);
            return siteBaseInfo;
        }

        public int update(SiteBaseInfo siteBaseInfo) {
            siteBaseInfo.setModifyDate(new Date());
            int rows = super.update(siteBaseInfo);
            return rows;
        }

    }


    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteIp() {
        return siteIp;
    }

    public void setSiteIp(String siteIp) {
        this.siteIp = siteIp;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }
}
