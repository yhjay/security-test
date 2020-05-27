package com.my.securitytest.config.security;

import com.my.securitytest.enums.ErrorEnum;
import com.my.securitytest.model.ErrorMsg;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * 自定义错误信息
 */
@SuppressWarnings("ALL")
@Component
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    @Override
    public ResponseEntity translate(Exception e) throws Exception {

        ErrorMsg errorMsg = new ErrorMsg("oauth_error","授权认证失败");
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        if(e instanceof BadCredentialsException){
            errorMsg = new ErrorMsg(ErrorEnum.ERROR_CLIENT.getErrorCode(),ErrorEnum.ERROR_CLIENT.getErrorMessage());
            status = HttpStatus.UNAUTHORIZED;
        }

        if(e instanceof InsufficientAuthenticationException){
            errorMsg = new ErrorMsg(ErrorEnum.ERROR_NEED_AUTH.getErrorCode(),ErrorEnum.ERROR_NEED_AUTH.getErrorMessage());
            status = HttpStatus.UNAUTHORIZED;
        }

        if(e instanceof InvalidGrantException){
            errorMsg = new ErrorMsg(ErrorEnum.ERROR_LOGIN.getErrorCode(),ErrorEnum.ERROR_LOGIN.getErrorMessage());
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity(errorMsg,status);


    }

}
