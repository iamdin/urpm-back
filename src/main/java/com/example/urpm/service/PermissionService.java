package com.example.urpm.service;

import com.example.urpm.model.dto.PermissionDto;

import java.util.List;
import java.util.Set;

public interface PermissionService extends BaseService<PermissionDto> {

    List<PermissionDto> selectPermissionByRoleId(Integer id);

    Set<String> selectPermissionsByUser(String account);
}
