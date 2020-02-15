package com.example.urpm.controller;

import com.example.urpm.common.base.RestResult;
import com.example.urpm.common.enums.RestResultEnum;
import com.example.urpm.model.dto.RoleDto;
import com.example.urpm.service.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author dingjinyang
 * @datetime 2020/2/14 13:37
 * @description 角色 Controller
 */
@Slf4j
@RequestMapping("/role")
@RestController
@Api(value = "角色接口", tags = "角色接口模块")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/all")
    @RequiresPermissions(logical = Logical.AND, value = {"role:select"})
    public RestResult<PageInfo<RoleDto>> getAllRole(
            @RequestParam @NotBlank(message = "pageNum不能为空") int pageNum,
            @RequestParam @NotBlank(message = "pageSize不能为空") int pageSize) {
        System.out.println(pageNum);
        System.out.println(pageSize);
        PageHelper.startPage(pageNum, pageSize);
        List<RoleDto> list = roleService.selectAll();
        PageInfo<RoleDto> pageInfo = new PageInfo<>(list);
        return new RestResult<>(RestResultEnum.SUCCESS, pageInfo);
    }
}
