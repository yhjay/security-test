package com.my.securitytest.model;


import com.my.securitytest.enums.ErrorEnum;

/**
 * @program: security-test
 * @description: 业务异常
 * @author: Thomas.Yang
 * @create: 2018-09-28 19:17
 **/
public class BizException extends RuntimeException {
    private ErrorEnum error = null;
    private String message;

    public BizException() {

    }

    public BizException(String message) {
        this.error = ErrorEnum.ERROR_BIZ;
        this.message = message;
    }

    public BizException(ErrorEnum error) {
        this.error = error;
//        this.message = error.getErrorMessage();
    }

    public BizException(ErrorEnum error, String message) {
        this.error = error;
        this.message = message;
    }

    public ErrorEnum getError() {
        return error;
    }

    public void setError(ErrorEnum error) {
        this.error = error;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
