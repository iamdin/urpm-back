package com.example.urpm.mapper;

import com.example.urpm.model.dto.PermissionDto;

import java.util.List;

public interface PermissionMapper extends tk.mybatis.mapper.common.Mapper<PermissionDto>, tk.mybatis.mapper.common.MySqlMapper<PermissionDto> {

    List<PermissionDto> selectPermissionByRoleId(Integer integer);
}