package com.example.urpm.controller;

import com.example.urpm.common.base.RestResult;
import com.example.urpm.common.enums.RestResultEnum;
import com.example.urpm.model.common.Constant;
import com.example.urpm.model.dto.PermissionDto;
import com.example.urpm.service.PermissionService;
import com.example.urpm.util.JWTUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

/**
 * @author dingjinyang
 * @datetime 2020/2/14 13:25
 * @description 权限 Controller
 */
@Slf4j
@RequestMapping("/permission")
@RestController
@Api(value = "权限接口", tags = "权限接口模块")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("{id}")
    @RequiresPermissions(logical = Logical.AND, value = {"permission:select"})
    public RestResult<PermissionDto> getPermissionById(@PathVariable Integer id) {
        PermissionDto permissionDto = new PermissionDto();
        permissionDto.setId(id);
        permissionDto = permissionService.selectOne(permissionDto);
        return new RestResult<>(RestResultEnum.SUCCESS, permissionDto);
    }

    @GetMapping("/user")
    @RequiresPermissions(logical = Logical.AND, value = {"permission:select"})
    public RestResult<Set<String>> getPerCodesByUser(@RequestHeader HttpHeaders headers) {
        String account = JWTUtil.getClaim(String.valueOf(headers.get("Authorization")), Constant.ACCOUNT);
        Set<String> stringSet = permissionService.selectPermissionsByUser(account);
        return new RestResult<>(RestResultEnum.SUCCESS, stringSet);
    }

    @GetMapping("/all")
    @RequiresPermissions(logical = Logical.AND, value = {"permission:select"})
    public RestResult<PageInfo<PermissionDto>> getAllPermission(
            @RequestParam @NotBlank(message = "pageNum不能为空") int pageNum,
            @RequestParam @NotBlank(message = "pageSize不能为空") int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<PermissionDto> list = permissionService.selectAll();
        PageInfo<PermissionDto> pageInfo = new PageInfo<>(list);
        return new RestResult<>(RestResultEnum.SUCCESS, pageInfo);
    }

    @PostMapping
    @RequiresPermissions(logical = Logical.AND, value = {"permission:insert"})
    public RestResult<Integer> insertUser(@RequestBody @Validated PermissionDto permissionDto) {
        int res;
        try {
            res = permissionService.insert(permissionDto);
        } catch (DuplicateKeyException e) {
            return new RestResult<>(RestResultEnum.FAIL, "权限已存在！");
        }
        return new RestResult<>(RestResultEnum.SUCCESS, res);
    }

    @PutMapping
    @RequiresPermissions(logical = Logical.AND, value = {"permission:update"})
    public RestResult<Integer> updateUser(@RequestBody @Validated PermissionDto permissionDto) {
        if (permissionDto.getId() == null) {
            return new RestResult<>(RestResultEnum.FAIL, "权限Id不能为空！");
        }
        int res;
        try {
            res = permissionService.updateByPrimaryKeySelective(permissionDto);
        } catch (DuplicateKeyException e) {
            return new RestResult<>(RestResultEnum.FAIL, "权限已存在！");
        }
        return new RestResult<>(
                res == 1 ? RestResultEnum.SUCCESS : RestResultEnum.FAIL
                , res);
    }

    @DeleteMapping("{id}")
    @RequiresPermissions(logical = Logical.AND, value = {"permission:delete"})
    public RestResult<Integer> deleteUser(@PathVariable Integer id) {
        int res = permissionService.deleteByPrimaryKey(id);
        if (res == 0) {
            return new RestResult<>(RestResultEnum.FAIL, "权限不存在！");
        }
        return new RestResult<>(RestResultEnum.SUCCESS, res);
    }
}
