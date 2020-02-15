package com.example.urpm.util;

import com.example.urpm.common.exception.BusinessException;
import com.example.urpm.model.common.Constant;
import com.example.urpm.model.dto.UserDto;
import com.example.urpm.service.UserService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author dingjinyang
 * @datetime 2020/2/13 11:55
 * @description 用户工具类
 */
@Getter
@Slf4j
@Component
public class UserUtil {

    @Resource
    private UserService userService;

    /**
     * 获取当前登录用户
     * @return UserDto
     */
    public UserDto getUser() {
        String token = SecurityUtils.getSubject().getPrincipal().toString();
        // 解密获得Account
        String account = JWTUtil.getClaim(token, Constant.ACCOUNT);
        UserDto userDto = userService.selectUserByAccount(account);
        // 用户是否存在
        if (userDto == null) {
            throw new BusinessException(" 该帐号不存在(The account does not exist.)");
        }
        return userDto;
    }

    /**
     * 获取当前登录用户Id
     * @return
     */
    public Integer getUserId() {
        return getUser().getId();
    }

    /**
     * 获取当前登录用户Token
     * @return
     */
    public String getToken() {
        return SecurityUtils.getSubject().getPrincipal().toString();
    }

    /**
     * 获取当前登录用户Account
     * @return
     */
    public String getAccount() {
        String token = SecurityUtils.getSubject().getPrincipal().toString();
        // 解密获得Account
        return JWTUtil.getClaim(token, Constant.ACCOUNT);
    }

}
