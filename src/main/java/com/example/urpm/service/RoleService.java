package com.example.urpm.service;

import com.example.urpm.model.dto.RoleDto;

import java.util.List;
import java.util.Set;

public interface RoleService extends BaseService<RoleDto> {

    List<RoleDto> selectRoleByUserAccount(String account);

    Set<String> selectRoleNamesByUserAccount(String account);
}
