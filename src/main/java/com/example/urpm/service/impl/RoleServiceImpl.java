package com.example.urpm.service.impl;

import com.example.urpm.mapper.RoleMapper;
import com.example.urpm.model.dto.RoleDto;
import com.example.urpm.service.RoleService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author dingjinyang
 * @datetime 2020/2/12 10:16
 * @description RoleService Implement
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleDto> implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<RoleDto> selectRoleByUserAccount(String account) {
        return roleMapper.selectRoleByUserAccount(account);
    }

    @Override
    public Set<String> selectRoleNamesByUserAccount(String account) {
        List<RoleDto> roleDtos = roleMapper.selectRoleByUserAccount(account);
        Set<String> strings = new HashSet<>();
        roleDtos.forEach(roleDto -> strings.add(roleDto.getName()));
        return strings;
    }
}
