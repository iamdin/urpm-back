CREATE SCHEMA urpm;
USE urpm;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源名称',
  `per_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限代码字符串',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `per_code`(`per_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, '查看用户', 'user:select');
INSERT INTO `permission` VALUES (2, '更新用户', 'user:update');
INSERT INTO `permission` VALUES (3, '新增用户', 'user:insert');
INSERT INTO `permission` VALUES (4, '删除用户', 'user:delete');
INSERT INTO `permission` VALUES (5, '查看角色', 'role:select');
INSERT INTO `permission` VALUES (6, '更新角色', 'role:update');
INSERT INTO `permission` VALUES (7, '新增角色', 'role:insert');
INSERT INTO `permission` VALUES (8, '删除角色', 'role:delete');
INSERT INTO `permission` VALUES (9, '查看权限', 'permission:select');
INSERT INTO `permission` VALUES (10, '更新权限', 'permission:update');
INSERT INTO `permission` VALUES (11, '新增权限', 'permission:insert');
INSERT INTO `permission` VALUES (12, '删除权限', 'permission:delete');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (2, 'admin');
INSERT INTO `role` VALUES (1, 'root');
INSERT INTO `role` VALUES (3, 'user');

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` int(0) NOT NULL COMMENT '角色id',
  `permission_id` int(0) NOT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  INDEX `permission_id`(`permission_id`) USING BTREE,
  CONSTRAINT `role_permission_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `role_permission_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES (1, 1, 1);
INSERT INTO `role_permission` VALUES (2, 1, 2);
INSERT INTO `role_permission` VALUES (3, 1, 3);
INSERT INTO `role_permission` VALUES (5, 1, 4);
INSERT INTO `role_permission` VALUES (6, 1, 5);
INSERT INTO `role_permission` VALUES (7, 1, 6);
INSERT INTO `role_permission` VALUES (8, 1, 7);
INSERT INTO `role_permission` VALUES (9, 1, 8);
INSERT INTO `role_permission` VALUES (10, 1, 9);
INSERT INTO `role_permission` VALUES (11, 1, 10);
INSERT INTO `role_permission` VALUES (12, 1, 11);
INSERT INTO `role_permission` VALUES (13, 1, 12);
INSERT INTO `role_permission` VALUES (14, 2, 1);
INSERT INTO `role_permission` VALUES (15, 2, 2);
INSERT INTO `role_permission` VALUES (16, 2, 3);
INSERT INTO `role_permission` VALUES (17, 2, 5);
INSERT INTO `role_permission` VALUES (18, 2, 6);
INSERT INTO `role_permission` VALUES (19, 2, 7);
INSERT INTO `role_permission` VALUES (20, 2, 9);
INSERT INTO `role_permission` VALUES (21, 3, 1);
INSERT INTO `role_permission` VALUES (22, 3, 5);
INSERT INTO `role_permission` VALUES (23, 3, 9);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `account` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '帐号',
  `password` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '昵称',
  `reg_time` datetime(0) NOT NULL COMMENT '注册时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `account`(`account`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'root', 'NzgwQjlCRDI5Q0QxNEZDRUQyNzZFMkYyQkRCM0I3OTg=', 'root', '2020-02-09 06:59:14');
INSERT INTO `user` VALUES (2, 'admin', 'QUM5RjQ4RDM5Mzk2ODA3MDkzNDg1MkI5OEM0MkE5REU=', 'admin', '2020-02-09 06:59:14');
INSERT INTO `user` VALUES (3, 'user', 'OUQ4NEIwOEVFM0JBMTVFNDYxNkUzQjhDQkMzNEYwMDE=', 'user', '2020-02-09 06:59:14');
INSERT INTO `user` VALUES (44, 'zhang', 'RTBGRDM1RDA0QjJEMjJBRUE4NThDQzI2NzU5NTI1MkI=', 'ding', '2020-02-14 14:26:18');
INSERT INTO `user` VALUES (46, 'guest', 'Q0Q5NDY3OTc2MzZEQUIyQjNENDY4RjgwN0NEQjJDODU=', 'guest', '2020-02-14 14:55:36');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `role_id` int(0) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  CONSTRAINT `user_role_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user_role_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 1, 1);
INSERT INTO `user_role` VALUES (2, 2, 2);
INSERT INTO `user_role` VALUES (3, 3, 3);

SET FOREIGN_KEY_CHECKS = 1;
