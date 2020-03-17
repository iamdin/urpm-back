package com.example.urpm.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.urpm.common.base.RestResult;
import com.example.urpm.common.enums.RestResultEnum;
import com.example.urpm.config.ConfigProperties;
import com.example.urpm.model.common.Constant;
import com.example.urpm.model.dto.UserDto;
import com.example.urpm.model.valid.UserEditValidGroup;
import com.example.urpm.model.valid.UserLoginValidGroup;
import com.example.urpm.service.PermissionService;
import com.example.urpm.service.RoleService;
import com.example.urpm.service.UserService;
import com.example.urpm.util.JWTUtil;
import com.example.urpm.util.JedisUtil;
import com.example.urpm.util.common.AesCipherUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 * @author dingjinyang
 * @datetime 2020/2/11 21:13
 * @description User Controller
 */
@Slf4j
@RequestMapping("/user")
@RestController
@Api(value = "用户接口", tags = "用户接口模块")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final PermissionService permissionService;
    private final JedisUtil jedisUtil;

    public UserController(UserService userService, RoleService roleService, PermissionService permissionService, JedisUtil jedisUtil) {
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.jedisUtil = jedisUtil;
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "输入用户密码")
    public RestResult<Object> login(@RequestBody @Validated({UserLoginValidGroup.class})
                                            UserDto userDto) {
        UserDto user = new UserDto();
        user = userService.selectUserByAccount(userDto.getAccount());
        if (user == null) {
            return new RestResult<>(RestResultEnum.FAIL, "账号不存在！");
        }
        // 密码进行AES解密
        String key = AesCipherUtil.deCrypto(user.getPassword());
        if (!Objects.equals(key, userDto.getAccount() + userDto.getPassword())) {
            return new RestResult<>(RestResultEnum.FAIL, "密码错误！");
        }

        // 清除可能存在的Shiro权限信息缓存
        if (jedisUtil.exists(Constant.PREFIX_SHIRO_CACHE + userDto.getAccount())) {
            jedisUtil.delKey(Constant.PREFIX_SHIRO_CACHE + userDto.getAccount());
        }

        // 设置RefreshToken，时间戳为当前时间戳，直接设置即可(不用先删后设，会覆盖已有的RefreshToken)
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        jedisUtil.set(Constant.PREFIX_SHIRO_REFRESH_TOKEN + userDto.getAccount(), currentTimeMillis, ConfigProperties.refreshTokenExpireTime);

        // 根据账号生成Token
        String token = JWTUtil.sign(userDto.getAccount(), currentTimeMillis);
        Map<String, String> res = new HashMap<>();
        res.put("token", token);
        return new RestResult<>(RestResultEnum.SUCCESS, "登录成功！", res);
    }

    @GetMapping("{id}")
    @RequiresPermissions(logical = Logical.AND, value = {"user:select"})
    public RestResult<UserDto> getUserById(@PathVariable Integer id) {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto = userService.selectOne(userDto);
        return new RestResult<>(RestResultEnum.SUCCESS, userDto);
    }

    @GetMapping("/all")
    @RequiresPermissions(logical = Logical.AND, value = {"user:select"})
    public RestResult<PageInfo<UserDto>> getAllUser(
            @RequestParam @NotBlank(message = "pageNum不能为空") int pageNum,
            @RequestParam @NotBlank(message = "pageSize不能为空") int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserDto> list = userService.selectAll();
        PageInfo<UserDto> pageInfo = new PageInfo<>(list);
        return new RestResult<>(RestResultEnum.SUCCESS, pageInfo);
    }

    @GetMapping("/info")
    @RequiresAuthentication
    public RestResult<Map<String, Object>> getUserInfo(@RequestHeader HttpHeaders headers) {
        String account = JWTUtil.getClaim(String.valueOf(headers.get("Authorization")), Constant.ACCOUNT);
        //用户信息
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        UserDto userDto = userService.selectUserByAccount(account);
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(userDto, SerializerFeature.WriteDateUseDateFormat));
        Objects.requireNonNull(map).remove("password");
        map.put("avatar", "https://d36jcksde1wxzq.cloudfront.net/saas-mega/whiteFingerprint.png");
        // 用户角色
        Set<String> roleDtos = roleService.selectRoleNamesByUserAccount(account);
        map.put("roles", roleDtos);
        // 获取用户权限
        Set<String> permissions = permissionService.selectPermissionsByUser(account);
        map.put("permissions", permissions);
        return new RestResult<>(RestResultEnum.SUCCESS, map);
    }

    @PostMapping
    @RequiresPermissions(logical = Logical.AND, value = {"user:insert"})
    public RestResult<Integer> insertUser(@RequestBody @Validated({UserLoginValidGroup.class, UserEditValidGroup.class}) UserDto userDto) {
        userDto.setRegTime(new Date());
        int res;
        try {
            res = userService.insert(userDto);
        } catch (DuplicateKeyException e) {
            return new RestResult<>(RestResultEnum.FAIL, "账号已存在！");
        }
        return new RestResult<>(RestResultEnum.SUCCESS, res);
    }

    @PutMapping
    @RequiresPermissions(logical = Logical.AND, value = {"user:update"})
    public RestResult<Integer> updateUser(@RequestBody UserDto userDto) {
        if (userDto.getId() == null) {
            return new RestResult<>(RestResultEnum.FAIL, "用户Id不能为空！");
        }
        int res = userService.updateByPrimaryKeySelective(userDto);
        return new RestResult<>(
                res == 1 ? RestResultEnum.SUCCESS : RestResultEnum.FAIL
                , res);
    }

    @DeleteMapping("{id}")
    @RequiresPermissions(logical = Logical.AND, value = {"user:delete"})
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Integer", paramType = "path")
    public RestResult<Integer> deleteUser(@PathVariable Integer id) {
        int res = userService.deleteByPrimaryKey(id);
        if (res == 0) {
            return new RestResult<>(RestResultEnum.FAIL, "账号不存在！");
        }
        return new RestResult<>(RestResultEnum.SUCCESS, res);
    }


}
