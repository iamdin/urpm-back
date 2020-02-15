package com.example.urpm.mapper;

import com.example.urpm.model.dto.UserDto;

public interface UserMapper extends tk.mybatis.mapper.common.Mapper<UserDto>, tk.mybatis.mapper.common.MySqlMapper<UserDto> {

    public UserDto selectUserByAccount(String account);
}