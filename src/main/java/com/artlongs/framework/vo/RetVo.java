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
public class RetVo<T> {

    private RetCode retcode = RetCode.SUCCESS;
    private String ref= "";  //页面跳转地址
    private String msg= "";  //返回提示信息
    private T item;//返回对象(单个或列表)

    public RetVo() {
    }

    public RetVo(RetCode retcode) {
        this(retcode, null, "", null);
    }

    public RetVo(RetCode retcode, T item) {
        this(retcode, null,"", item);
    }


    public RetVo(RetCode retcode, String msg) {
        this(retcode, msg, "", null);
    }

    public RetVo(RetCode retcode, String msg, String ref) {
        this(retcode, msg, ref,null);
    }

    public RetVo(RetCode retcode, String msg, String ref, T item) {
        this.retcode = retcode;
        this.ref = ref;
        this.item = item;
        if (msg != null && msg.trim().length() > 0) {
            this.setMsg(msg);
        }
    }

    /**
     * 注意这个NEW了一下,你明白的
     * @param msg
     * @return
     */
    public static RetVo error(String msg) {
        RetVo vo = new RetVo();
        vo.setMsg(msg);
        vo.setRetcode(RetCode.FAIL);
        return vo;
    }

    /**
     * 注意这个NEW了一下,你明白的
     * @param msg
     * @return
     */
    public static RetVo success(String msg) {
        RetVo vo = new RetVo();
        vo.setRetcode(RetCode.SUCCESS);
        vo.setMsg(msg);
        return vo;
    }

    public RetVo setError(String msg) {
        this.setRetcode(RetCode.FAIL);
        this.setMsg(msg);
        return this;
    }

    public RetVo setSuccess(String msg) {
        this.setRetcode(RetCode.SUCCESS);
        this.setMsg(msg);
        return this;
    }

    public boolean isSucc(){
        return RetCode.SUCCESS == retcode;
    }

    public RetCode getRetcode() {
        return retcode;
    }

    public RetVo setRetcode(RetCode retcode) {
        this.retcode = retcode;
        return this;
    }

    public String getMsg() {
        return (null==this.msg || msg.trim().length() == 0)? retcode.getMsg():this.msg;
    }

    public RetVo setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getRef() {
        return ref;
    }

    public RetVo setRef(String ref) {
        this.ref = ref;
        return this;
    }

    public T getItem() {
        return item;
    }

    public RetVo setItem(T item) {
        this.item = item;
        return this;
    }

    @Override
    public String toString() {
        return "BizRetVo{" +
                "retcode=" + retcode +
                ", ref='" + ref +
                ", msg='" + msg +
                ", item=" + item +
                '}';
    }
}
