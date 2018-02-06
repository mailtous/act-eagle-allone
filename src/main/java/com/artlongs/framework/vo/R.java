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

    private Rcode retcode = Rcode.SUCCESS;
    private String ref= "";  //页面跳转地址
    private String msg= "";  //返回提示信息
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
        this.retcode = retcode;
        this.ref = ref;
        this.item = item;
        if (msg != null && msg.trim().length() > 0) {
            this.setMsg(msg);
        }
    }

    public R setTF(boolean tf) {
       return tf ? setRetcode(Rcode.SUCCESS):setRetcode(Rcode.FAIL);
    }

    public R setTF(int number) {
        return number>0 ? setRetcode(Rcode.SUCCESS):setRetcode(Rcode.FAIL);
    }

    /**
     * 注意这个NEW了一下,你明白的
     * @param msg
     * @return
     */
    public static R fail(String msg) {
        R vo = new R();
        vo.setMsg(msg);
        vo.setRetcode(Rcode.FAIL);
        return vo;
    }

    /**
     * 注意这个NEW了一下,你明白的
     * @param msg
     * @return
     */
    public static R success(String msg) {
        R vo = new R();
        vo.setRetcode(Rcode.SUCCESS);
        vo.setMsg(msg);
        return vo;
    }

    public R setFail(String msg) {
        this.setRetcode(Rcode.FAIL);
        this.setMsg(msg);
        return this;
    }

    public R setSuccess(String msg) {
        this.setRetcode(Rcode.SUCCESS);
        this.setMsg(msg);
        return this;
    }

    public boolean isSucc(){
        return Rcode.SUCCESS == retcode;
    }

    public Rcode getRetcode() {
        return retcode;
    }

    public R setRetcode(Rcode retcode) {
        this.retcode = retcode;
        return this;
    }

    public String getMsg() {
        return (null==this.msg || msg.trim().length() == 0)? retcode.getMsg():this.msg;
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
        return "R{" +
                "retcode=" + retcode +
                ", ref='" + ref + '\'' +
                ", msg='" + msg + '\'' +
                ", item=" + item +
                '}';
    }
}
