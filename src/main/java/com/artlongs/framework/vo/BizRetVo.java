package com.artlongs.framework.vo;

/**
 * <p>Function:业务层统一的返回值包装VO</p>
 *
 * @version $Revision$ $Date$
 *          Date: 15-5-28
 *          Time: 下午12:25
 * @author: lqf
 * @since 1.0
 */
public class BizRetVo<T> {

    private BizRetCode retcode = BizRetCode.SUCCESS;
    private String ref= "";  //页面跳转地址
    private String msg= "";  //返回提示信息
    private T item;//返回对象(单个或列表)

    public BizRetVo() {
    }

    public BizRetVo(BizRetCode retcode) {
        this(retcode, null, "", null);
    }

    public BizRetVo(BizRetCode retcode, T item) {
        this(retcode, null,"", item);
    }


    public BizRetVo(BizRetCode retcode, String msg) {
        this(retcode, msg, "", null);
    }

    public BizRetVo(BizRetCode retcode, String msg, String ref) {
        this(retcode, msg, ref,null);
    }

    public BizRetVo(BizRetCode retcode, String msg, String ref, T item) {
        this.retcode = retcode;
        this.ref = ref;
        this.item = item;
        if (msg != null && msg.trim().length() > 0) {
            this.setMsg(msg);
        }
    }
    public BizRetVo setError(String msg) {
        retcode = BizRetCode.FAIL;
        setMsg(msg);
        return this;
    }

    public BizRetVo setSuccess(String msg) {
        retcode = BizRetCode.SUCCESS;
        setMsg(msg);
        return this;
    }

    public boolean isSucc(){
        return BizRetCode.SUCCESS == retcode;
    }

    public BizRetCode getRetcode() {
        return retcode;
    }

    public void setRetcode(BizRetCode retcode) {
        this.retcode = retcode;
    }

    public String getMsg() {
        return (null==this.msg || msg.trim().length() == 0)? retcode.getMsg():this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }


}
