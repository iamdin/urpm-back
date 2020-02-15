package com.example.urpm.service.impl;

import com.example.urpm.mapper.UserMapper;
import com.example.urpm.model.dto.UserDto;
import com.example.urpm.service.UserService;
import com.example.urpm.util.common.AesCipherUtil;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author dingjinyang
 * @datetime 2020/2/11 21:21
 * @description User Service
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserDto> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public int insert(UserDto userDto) {
        String account = userDto.getAccount();
        String password = userDto.getPassword();
        // 账号加密码加密
        userDto.setPassword(AesCipherUtil.enCrypto(account + password));
        return userMapper.insert(userDto);
    }

    /**
     * 根据用户名 查询用户
     * @param account String
     * @return UserDto
     */
    public UserDto selectUserByAccount(String account) {
        return userMapper.selectUserByAccount(account);
    }
}
