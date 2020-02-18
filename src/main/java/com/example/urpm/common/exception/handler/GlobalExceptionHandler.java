package com.example.urpm.common.exception.handler;

import com.example.urpm.common.base.RestResult;
import com.example.urpm.common.enums.RestResultEnum;
import com.example.urpm.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.constraints.Null;

/**
 * @author dingjinyang
 * @datetime 2020/2/18 15:34
 * @description 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务逻辑错误
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResult<Null> handleBusinessException(BusinessException e) {
        return new RestResult<>(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
    }

}
