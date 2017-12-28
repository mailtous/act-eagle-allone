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

    /**
     * 注意这个NEW了一下,你明白的
     * @param msg
     * @return
     */
    public static BizRetVo error(String msg) {
        BizRetVo vo = new BizRetVo();
        vo.setMsg(msg);
        vo.setRetcode(BizRetCode.FAIL);
        return vo;
    }

    /**
     * 注意这个NEW了一下,你明白的
     * @param msg
     * @return
     */
    public static BizRetVo success(String msg) {
        BizRetVo vo = new BizRetVo();
        vo.setRetcode(BizRetCode.SUCCESS);
        vo.setMsg(msg);
        return vo;
    }

    public BizRetVo setError(String msg) {
        this.setRetcode(BizRetCode.FAIL);
        this.setMsg(msg);
        return this;
    }

    public BizRetVo setSuccess(String msg) {
        this.setRetcode(BizRetCode.SUCCESS);
        this.setMsg(msg);
        return this;
    }

    public boolean isSucc(){
        return BizRetCode.SUCCESS == retcode;
    }

    public BizRetCode getRetcode() {
        return retcode;
    }

    public BizRetVo setRetcode(BizRetCode retcode) {
        this.retcode = retcode;
        return this;
    }

    public String getMsg() {
        return (null==this.msg || msg.trim().length() == 0)? retcode.getMsg():this.msg;
    }

    public BizRetVo setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getRef() {
        return ref;
    }

    public BizRetVo setRef(String ref) {
        this.ref = ref;
        return this;
    }

    public T getItem() {
        return item;
    }

    public BizRetVo setItem(T item) {
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
