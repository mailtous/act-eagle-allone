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
    SUCCESS("操作成功"),
    /**
     * 操作失败
     */
    FAIL("操作失败"),

    /**
     * 用户未登录或超时退出，请重新登录
     */
    UNLOGIN("用户未登录或超时退出，请重新登录"),

    /**
     * 用户信息不存在
     */
    USER_NOT_EXIST("用户信息不存在"),

    /**
     * 你的手机号未验证
     */
    NOT_VERIFY_MOBILE("你的手机号未验证"),

    /**
     * 两次输入的密码不相同
     */
    PASSWORD_NOT_SAME("两次输入的密码不相同"),

    /**
     * 邮件地址已存在
     */
    EMAIL_EXIST("邮件地址已存在"),

    /**
     * 手机号码已存在
     */
    MOBILE_EXIST("手机号码已存在"),

    /**
     * 验证码错误
     */
    CAPTCHACODE_NOT_RIGHT("验证码错误"),

    /**
     * 手机验证码已经发送
     */
    SMS_VERIFYCODE_SENDED("验证码已发送，请稍后再尝试获取"),

    /**
     * 用户名不能为空
     */
    USER_NAME_EMPTY("用户名不能为空"),

    /**
     * 密码不能为空
     */
    PASSWORD_EMPTY("密码不能为空"),

    /**
     * 手机不能为空
     */
    MOBILE_EMPTY("手机不能为空"),

    /**
     * 手机号码格式不正确
     */
    MOBILE_FORMAT_INVALID("手机号码格式不正确"),

    /**
     * Email地址格式不正确
     */
    EMAIL_FORMAT_INVALID("Email地址格式不正确"),

    /**
     * 输入的信息不正确，请重新输入。
     */
    INPUT_INFO_NOT_RIGHT("输入的信息不正确，请重新输入。"),

    /**
     * 该账号已经注册，请登录并绑定。
     */
    ACCOUNT_EXIST_WECHAT("该账号已经注册，请登录并绑定。"),

    /**
     * 已到达底部。
     */
    PAGE_END("已到达底部。"),

    /**
     * 参数错误。
     */
    PARAM_ERROR("参数错误。"),

    /**
     * 取不出更多数据。
     */
    NO_MORE_DATA("取不出更多数据。");


    private String msg;

    private Rcode(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return null == msg ? "" : msg;
    }

    public void setMsgParam(String paramName, String paramValue) {
        msg = msg.replaceAll("${" + paramName + "}", paramValue);
    }

    @Override
    public String toString() {
        return "retCode{" +
                "msg='" + msg + '\'' +
                '}';
    }
}