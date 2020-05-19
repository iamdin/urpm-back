package com.example.urpm.common.exception.handler;

import com.example.urpm.common.base.R;
import com.example.urpm.common.enums.RCodeEnum;
import com.example.urpm.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author dingjinyang
 * @datetime 2020/2/18 15:34
 * @description 全局异常处理
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务逻辑错误
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public R handleGlobalException(BusinessException e) {
        return R.setR(RCodeEnum.SERVER_ERROR).data(e.getMessage());
    }

}
