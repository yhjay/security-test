package com.my.securitytest.model;

import com.my.securitytest.enums.ErrorEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: security-test
 * @description: 错误消息
 * @author: Thomas.Yang
 * @create: 2018-12-18 13:52
 **/
@Data
@ApiModel("ErrorMsg")
public class ErrorMsg {

    @ApiModelProperty(value = "错误码")
    private String code;
    @ApiModelProperty(value = "错误消息")
    private String msg;

    public ErrorMsg() {
    }

    public ErrorMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ErrorMsg(ErrorEnum errorEnum) {
        this.code = errorEnum.getErrorCode();
        this.msg = errorEnum.getErrorMessage();
    }
}
