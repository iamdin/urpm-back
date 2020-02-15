package com.example.urpm.mapper;

import com.example.urpm.model.dto.RoleDto;

import java.util.List;

public interface RoleMapper extends tk.mybatis.mapper.common.Mapper<RoleDto>, tk.mybatis.mapper.common.MySqlMapper<RoleDto> {

    List<RoleDto> selectRoleByUserAccount(String account);
}