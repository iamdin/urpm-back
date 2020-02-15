package com.example.urpm.model.dto;

import com.example.urpm.model.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;

/**
 * @author dingjinyang
 * @datetime 2020/2/11 21:41
 * @description User Dto
 */
@EqualsAndHashCode(callSuper = true)
@Table(name = "user")
@Data
public class UserDto extends User {
}
