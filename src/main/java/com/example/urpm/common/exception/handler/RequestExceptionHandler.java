package com.example.urpm.common.exception.handler;

import com.example.urpm.common.base.RestResult;
import com.example.urpm.common.enums.RestResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.constraints.Null;

/**
 * @author dingjinyang
 * @datetime 2020/2/12 14:38
 * @description 全局异常处理
 */

@Slf4j
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class RequestExceptionHandler {



    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResult<Null> handleConstraintViolationException(MissingServletRequestParameterException e) {
        return new RestResult<>(RestResultEnum.FAIL, "参数不能为空！");
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResult<Null> handleConstraintViolationException(MethodArgumentTypeMismatchException e) {
        return new RestResult<>(RestResultEnum.FAIL, "参数不合法！");
    }

    /**
     * 请求路径错误
     * @param e HttpRequestMethodNotSupportedException
     * @return RestResult
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public RestResult<Null> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return new RestResult<>(RestResultEnum.NOT_ALLOWED);
    }

    /**
     * 不存在的请求
     * @param e NoHandlerFoundException
     * @return RestResult
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public RestResult<Null> NoHandlerFoundException(NoHandlerFoundException e) {
        return new RestResult<>(RestResultEnum.NOT_FOUND);
    }

    /**
     * 参数校验不合法
     * @param e MethodArgumentNotValidException
     * @return RestResult
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestResult<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new RestResult<>(RestResultEnum.FAIL, e.getBindingResult().getFieldError().getDefaultMessage());
    }


}
