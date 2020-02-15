package com.example.urpm.service.impl;

import com.example.urpm.mapper.PermissionMapper;
import com.example.urpm.mapper.RoleMapper;
import com.example.urpm.mapper.UserMapper;
import com.example.urpm.model.dto.PermissionDto;
import com.example.urpm.model.dto.RoleDto;
import com.example.urpm.model.entity.Permission;
import com.example.urpm.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author dingjinyang
 * @datetime 2020/2/12 18:49
 * @description PermissionService
 */
@Service
public class PermissionServiceImpl extends BaseServiceImpl<PermissionDto> implements PermissionService {


    private PermissionMapper permissionMapper;

    @Autowired
    public void setPermissionMapper(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<PermissionDto> selectPermissionByRoleId(Integer id) {
        return permissionMapper.selectPermissionByRoleId(id);
    }

    @Override
    public Set<String> selectPermissionsByUser(String account) {
        List<RoleDto> roleDtos = roleMapper.selectRoleByUserAccount(account);
        Set<String> perCodeSet = new HashSet<>();
        roleDtos.forEach(roleDto -> {
            if (roleDto != null) {
                // 根据用户角色查询权限
                List<PermissionDto> permissionDtos = permissionMapper.selectPermissionByRoleId(roleDto.getId());
                permissionDtos.forEach(permissionDto -> {
                    if (permissionDto != null) {
                        // 添加权限
                        perCodeSet.add(permissionDto.getPerCode());
                    }
                });
            }
        });
        return perCodeSet;
    }
}
