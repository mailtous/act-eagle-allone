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
public class R<T> {

    private Integer code = Rcode.SUCCESS.getCode();
    private String ref= "";  //页面跳转地址
    private String msg= Rcode.SUCCESS.getMsg();;  //返回提示信息
    private T item;//返回对象(单个或列表)

    public R() {
    }

    public R(Rcode retcode) {
        this(retcode, null, "", null);
    }

    public R(Rcode retcode, T item) {
        this(retcode, null,"", item);
    }


    public R(Rcode retcode, String msg) {
        this(retcode, msg, "", null);
    }

    public R(Rcode retcode, String msg, String ref) {
        this(retcode, msg, ref,null);
    }

    public R(Rcode retcode, String msg, String ref, T item) {
        this.ref = ref;
        this.item = item;
        if (msg != null && msg.trim().length() > 0) {
            this.setMsg(msg);
        }
    }

    public R setTF(boolean tf) {
       return tf ? setCode(Rcode.SUCCESS.getCode()):setCode(Rcode.FAIL.getCode());
    }

    public R setTF(int number) {
        return number>0 ? setCode(Rcode.SUCCESS.getCode()):setCode(Rcode.FAIL.getCode());
    }

    public static R tf(boolean tf) {
        return new R().setTF(tf);
    }
    public static R tf(int number) {
        return new R().setTF(number);
    }

    /**
     * 注意这个NEW了一下,你明白的
     * @param msg
     * @return
     */
    public static R fail(String msg) {
        R vo = new R();
        vo.setMsg(msg);
        vo.setCode(Rcode.FAIL.getCode());
        return vo;
    }

    /**
     * 注意这个NEW了一下,你明白的
     * @param msg
     * @return
     */
    public static R success(String msg) {
        R vo = new R();
        vo.setCode(Rcode.SUCCESS.getCode());
        vo.setMsg(msg);
        return vo;
    }

    public R setFail(String msg) {
        this.setCode(Rcode.FAIL.getCode());
        this.setMsg(msg);
        return this;
    }

    public R setSuccess(String msg) {
        this.setCode(Rcode.SUCCESS.getCode());
        this.setMsg(msg);
        return this;
    }

    public boolean isSucc(){
        return Rcode.SUCCESS.getCode() == code;
    }

    public Integer getCode() {
        return code;
    }

    public R setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return this.msg;
    }

    public R setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getRef() {
        return ref;
    }

    public R setRef(String ref) {
        this.ref = ref;
        return this;
    }

    public T getItem() {
        return item;
    }

    public R setItem(T item) {
        this.item = item;
        return this;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("R{");
        sb.append("code=").append(code);
        sb.append(", ref='").append(ref).append('\'');
        sb.append(", msg='").append(msg).append('\'');
        sb.append(", item=").append(item);
        sb.append('}');
        return sb.toString();
    }
}
