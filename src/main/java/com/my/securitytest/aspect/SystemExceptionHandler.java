package com.my.securitytest.aspect;

import com.my.securitytest.enums.ErrorEnum;
import com.my.securitytest.model.BizException;
import com.my.securitytest.model.ErrorMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * @program: security-test
 * @description: 异常统一处理
 * @author: Thomas.Yang
 * @create: 2018-10-31 08:37
 **/

@RestControllerAdvice
@Slf4j
public class SystemExceptionHandler {
    private static Pattern pat = Pattern.compile("[\u4e00-\u9fa5]");


    /**
     * 系统异常处理
     *
     * @param req
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorMsg> defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {

        HttpStatus httpStatus;
        ErrorMsg errorMsg;

        // 业务逻辑错误
        if (e instanceof BizException) {
            httpStatus = HttpStatus.CONFLICT;
            BizException bizException = (BizException) e;
            errorMsg = new ErrorMsg(bizException.getError().getErrorCode(), bizException.getMessage());
        }

        // 请求不存在
        else if (e instanceof NoHandlerFoundException) {
            httpStatus = HttpStatus.NOT_FOUND;
            errorMsg = new ErrorMsg(String.valueOf(httpStatus.value()), "请求的资源不存在");
        }
        // 消息不可读
        else if (e instanceof HttpMessageNotReadableException) {
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            errorMsg = new ErrorMsg(ErrorEnum.ERROR_VALID);
        }
        // 参数校验异常
        else if (e instanceof MethodArgumentNotValidException) {
            StringBuilder sb = new StringBuilder();
            ((MethodArgumentNotValidException) e).getBindingResult()
                    .getAllErrors().forEach(x -> sb.append(x.getDefaultMessage()).append(","));
            String strErrMsg = sb.toString();
            strErrMsg = strErrMsg.length() == 0 ? "" : strErrMsg.substring(0, strErrMsg.lastIndexOf(","));
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            errorMsg = new ErrorMsg(ErrorEnum.ERROR_VALID.getErrorCode(), strErrMsg);
        }
        // 参数类型异常
        else if (e instanceof MethodArgumentTypeMismatchException) {
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            errorMsg = new ErrorMsg(ErrorEnum.ERROR_PARAM);
        }
        // 没有权限
        else if(e instanceof AccessDeniedException){
            httpStatus = HttpStatus.FORBIDDEN;
            errorMsg = new ErrorMsg(ErrorEnum.ERROR_PERMISSION_DENIED.getErrorCode()
                    ,ErrorEnum.ERROR_PERMISSION_DENIED.getErrorMessage());
        }

        // 其他异常
        else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            errorMsg = new ErrorMsg(String.valueOf(httpStatus.value()), "系统错误");
            log.error("拦截到错误:", e);
        }

        return new ResponseEntity<>(errorMsg, httpStatus);

    }

}
