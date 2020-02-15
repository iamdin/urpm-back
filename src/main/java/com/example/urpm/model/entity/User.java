package com.example.urpm.model.entity;

import com.example.urpm.model.valid.UserEditValidGroup;
import com.example.urpm.model.valid.UserLoginValidGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Table(name = "user")
public class User  {

    /**
     * ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 帐号
     */
    @NotNull(message = "帐号不能为空", groups = {UserLoginValidGroup.class, UserEditValidGroup.class})
    private String account;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空", groups = {UserLoginValidGroup.class, UserEditValidGroup.class})
    private String password;

    /**
     * 昵称
     */
    @NotNull(message = "用户名不能为空", groups = {UserEditValidGroup.class})
    private String username;

    /**
     * 注册时间
     */
    @Column(name = "reg_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date regTime;


    /**
     * 设置帐号
     * @param account 帐号
     */
    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    /**
     * 设置密码
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 设置昵称
     * @param username 昵称
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }


}