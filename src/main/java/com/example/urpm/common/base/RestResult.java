package com.example.urpm.common.base;

import com.example.urpm.common.enums.RestResultEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author dingjinyang
 * @datetime 2020/2/12 13:45
 * @description Restful Result
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestResult<T> {

    private int code;

    private String msg;

    private T data;

    public RestResult(RestResultEnum e) {
        this.code = e.getCode();
        this.msg = e.getMessage();
        this.data = null;
    }

    public RestResult(RestResultEnum e, T data) {
        this.code = e.getCode();
        this.msg = e.getMessage();
        this.data = data;
    }

    public RestResult(RestResultEnum e, String msg) {
        this.code = e.getCode();
        this.msg = msg;
        this.data = null;
    }

    public RestResult(RestResultEnum e, String msg, T data) {
        this.code = e.getCode();
        this.msg = msg;
        this.data = data;
    }

    public RestResult(HttpStatus httpStatus) {
        this.code = httpStatus.value();
        this.msg = httpStatus.getReasonPhrase();
        this.data = null;
    }

    public RestResult(HttpStatus httpStatus, String msg) {
        this.code = httpStatus.value();
        this.msg = msg;
        this.data = null;
    }

    public RestResult(HttpStatus httpStatus, String msg, T data) {
        this.code = httpStatus.value();
        this.msg = msg;
        this.data = data;
    }
}
