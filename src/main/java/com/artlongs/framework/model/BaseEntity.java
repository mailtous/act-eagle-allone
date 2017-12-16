package com.artlongs.framework.model;

import org.beetl.sql.core.annotatoin.AutoID;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by leeton on 10/10/17.
 */
public class BaseEntity implements Serializable {
    @AutoID
    private Long id;
    private Date createDate;
    private Date modifyDate;

    public final static Integer DELETED = -1;
    public final static Integer UN_DEL = 0;


    public BaseEntity() {
        createDate = new Date();
        modifyDate = new Date();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                '}';
    }
}
