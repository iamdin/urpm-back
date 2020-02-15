package com.example.urpm.service;

import com.example.urpm.model.dto.UserDto;

public interface UserService extends BaseService<UserDto> {

    @Override
    int insert(UserDto userDto);

    UserDto selectUserByAccount(String account);
}
