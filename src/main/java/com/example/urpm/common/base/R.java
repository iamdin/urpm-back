package com.example.urpm.common.base;

import com.example.urpm.common.enums.RCodeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回内容类
 */
@Data
@Accessors(chain = true)
public class R<T> {

    private Integer code;

    private String msg;

    private T data;

    public static R success() {
        return new R().setCode(RCodeEnum.SUCCESS.getCode()).setMsg(RCodeEnum.SUCCESS.getMsg());
    }

    public static R failed() {
        return new R().setCode(RCodeEnum.FAIL.getCode()).setMsg(RCodeEnum.FAIL.getMsg());
    }

    public static R setR(RCodeEnum rCodeEnum) {
        return new R().setCode(rCodeEnum.getCode()).setMsg(rCodeEnum.getMsg());
    }

    public R code(Integer code) {
        this.setCode(code);
        return this;
    }

    public R msg(String msg) {
        this.setMsg(msg);
        return this;
    }

    public R data(T data) {
        this.setData(data);
        return this;
    }

}
