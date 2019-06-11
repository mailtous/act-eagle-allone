package com.artlongs.framework.vo;



/**
 * <p>返回代码封装</p>
 *
 * @author lqf
 * @since 1.0
 */
public enum Rcode {

    /**
     * 操作成功
     */
    SUCCESS(1,"操作成功"),
    /**
     * 操作失败
     */
    FAIL(0,"操作失败"),

    /**
     * 用户未登录或超时退出，请重新登录
     */
    UNLOGIN(1001,"用户未登录或超时退出，请重新登录"),

    /**
     * 用户信息不存在
     */
    USER_NOT_EXIST(1002,"用户信息不存在"),

    /**
     * 你的手机号未验证
     */
    NOT_VERIFY_MOBILE(1003,"你的手机号未验证"),

    /**
     * 两次输入的密码不相同
     */
    PASSWORD_NOT_SAME(1004,"两次输入的密码不相同"),

    /**
     * 邮件地址已存在
     */
    EMAIL_EXIST(1005,"邮件地址已存在"),

    /**
     * 手机号码已存在
     */
    MOBILE_EXIST(1006,"手机号码已存在"),

    /**
     * 验证码错误
     */
    CAPTCHACODE_NOT_RIGHT(1007,"验证码错误"),

    /**
     * 手机验证码已经发送
     */
    SMS_VERIFYCODE_SENDED(1008,"验证码已发送，请稍后再尝试获取"),

    /**
     * 用户名不能为空
     */
    USER_NAME_EMPTY(1009,"用户名不能为空"),

    /**
     * 密码不能为空
     */
    PASSWORD_EMPTY(1010,"密码不能为空"),

    /**
     * 手机不能为空
     */
    MOBILE_EMPTY(1011,"手机不能为空"),

    /**
     * 手机号码格式不正确
     */
    MOBILE_FORMAT_INVALID(1012,"手机号码格式不正确"),

    /**
     * Email地址格式不正确
     */
    EMAIL_FORMAT_INVALID(1013,"Email地址格式不正确"),

    /**
     * 输入的信息不正确，请重新输入。
     */
    INPUT_INFO_NOT_RIGHT(1014,"输入的信息不正确，请重新输入。"),

    /**
     * 该账号已经注册，请登录并绑定。
     */
    ACCOUNT_EXIST_WECHAT(1015,"该账号已经注册，请登录并绑定。"),

    /**
     * 已到达底部。
     */
    PAGE_END(1016,"已到达底部。"),

    /**
     * 参数错误。
     */
    PARAM_ERROR(1017,"参数错误。"),

    /**
     * 取不出更多数据。
     */
    NO_MORE_DATA(1018,"取不出更多数据。");


    private Integer code;
    private String msg;

    Rcode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Rcode{");
        sb.append("code=").append(code);
        sb.append(", msg='").append(msg).append('\'');
        sb.append('}');
        return sb.toString();
    }
}