package com.my.securitytest.enums;

/**
 * @program: security-test
 * @description: 自定义错误码
 * @author: Thomas.Yang
 * @create: 2018-09-28 19:17
 **/
public enum ErrorEnum {
    //
    ERROR_CLIENT("ERROR_CLIENT","客户端认证失败"),
    ERROR_LOGIN("ERROR_LOGIN","用户名或密码不正确"),
    ERROR_NEED_AUTH("ERROR_NEED_AUTH","需要授权认证"),
    ERROR_PERMISSION_DENIED("ERROR_PERMISSION_DENIED","权限不足"),
    ERROR_VALID("ERROR_VALID","参数验证失败"),
    ERROR_PARAM("ERROR_PARAM","参数错误"),
    ERROR_BIZ("ERROR_BIZ","业务逻辑错误"),
    ERROR_SYS("ERROR_SYS","系统错误"),

//    //注册
//    ERROR_CONTACT("1000","联系方式不正确"),
//    ERROR_CONTACT_REPEAT("1001","联系方式重复"),
//    ERROR_USER_REPEAT("1002","用户名重复"),
//    ERROR_CODE("1003","验证码错误"),
//    ERROR_PASSWORD("1004","密码与确认密码不匹配"),
    ;

    private final String errorCode;
    private final String errorMessage;



    ErrorEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
