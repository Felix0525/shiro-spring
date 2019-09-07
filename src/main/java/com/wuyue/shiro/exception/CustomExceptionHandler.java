package com.wuyue.shiro.exception;

import com.wuyue.shiro.DataResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.wuyue.shiro.StatusCode.AUTHORIZATION_ERROR;
import static com.wuyue.shiro.StatusCode.SYSTEM_ERROR;

@Slf4j
@ControllerAdvice
@ResponseBody
public class CustomExceptionHandler {

    @ExceptionHandler(BizException.class)
    public DataResp<Void> bizException(BizException ex) {
        log.error("Business exception found:", ex);
        return DataResp.build(ex.getCode().orElse(SYSTEM_ERROR.getCode()), ex.getMessage());
    }

    @ExceptionHandler(AuthorizationException.class)
    public DataResp<Void> authorizationException(AuthorizationException ex) {
        log.error("Authorization exception found:", ex);
        return DataResp.build(AUTHORIZATION_ERROR.getCode(), AUTHORIZATION_ERROR.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public DataResp<Void> systemException(Exception ex) {
        log.error("System exception found:", ex);
        return DataResp.build(SYSTEM_ERROR.getCode(), ex.getMessage());
    }

}
