/*
 Navicat Premium Data Transfer

 Source Server         : 172.28.1.3 内部DEV
 Source Server Type    : MySQL
 Source Server Version : 50621
 Source Host           : 172.28.1.3
 Source Database       : act_test

 Target Server Type    : MySQL
 Target Server Version : 50621
 File Encoding         : utf-8

 Date: 04/12/2018 15:33:18 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `sys_dept`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dept_name` varchar(100) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='部门表';

-- ----------------------------
--  Records of `sys_dept`
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` VALUES ('1', '研发部', '2017-12-04 10:50:02', '2017-12-04 10:50:04');
COMMIT;

-- ----------------------------
--  Table structure for `sys_func`
-- ----------------------------
DROP TABLE IF EXISTS `sys_func`;
CREATE TABLE `sys_func` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) DEFAULT NULL,
  `func_name` varchar(300) DEFAULT NULL,
  `func_url` varchar(500) DEFAULT NULL,
  `node` tinyint(2) DEFAULT NULL,
  `is_menu` tinyint(2) DEFAULT NULL,
  `action` tinyint(2) DEFAULT NULL,
  `sequence` int(5) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='功能点及菜单列表';

-- ----------------------------
--  Records of `sys_func`
-- ----------------------------
BEGIN;
INSERT INTO `sys_func` VALUES ('1', '0', '用户管理', null, '0', '1', '1', '1', '2017-12-04 16:41:24', '2017-12-04 16:41:27'), ('2', '1', '用户列表', '/sys/user/list', '1', '1', '1', '2', '2017-12-04 10:48:18', '2017-12-04 10:48:22'), ('3', '1', '增加用户', '/sys/user/add', '1', '0', '1', '3', '2018-02-07 17:08:03', '2018-02-07 17:08:03'), ('4', '1', '编辑用户资料', '/sys/user/edit/*', '1', '0', '1', '4', '2018-02-05 13:19:12', '2018-02-05 13:19:12'), ('5', '0', '系统管理', null, '0', '1', '1', '2', '2018-01-15 17:38:18', '2018-01-15 17:38:20'), ('6', '5', '菜单及功能管理', '/sys/func/list', '1', '1', '1', '10', '2018-02-05 15:31:47', '2018-02-05 15:31:47'), ('7', '5', '角色管理', '/sys/role/list', '1', '1', '1', '1', '2018-02-05 15:18:01', '2018-02-05 15:18:01'), ('9', '0', '首页', '/sys/home', '0', '1', '1', '1', '2018-04-10 14:04:59', '2018-04-10 14:04:59');
COMMIT;

-- ----------------------------
--  Table structure for `sys_permission`
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL,
  `func_id` int(11) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='权限表';

-- ----------------------------
--  Records of `sys_permission`
-- ----------------------------
BEGIN;
INSERT INTO `sys_permission` VALUES ('1', '1', '1', '2017-12-04 11:24:17', '2017-12-04 11:24:19'), ('2', '1', '2', '2017-12-04 11:25:03', '2017-12-04 11:25:04'), ('3', '1', '3', '2017-12-04 11:25:16', '2017-12-04 11:25:18'), ('4', '1', '4', '2017-12-04 17:15:25', '2017-12-04 17:15:26'), ('6', '1', '6', '2018-01-15 17:47:49', '2018-01-15 17:47:51'), ('7', '1', '7', '2018-02-05 15:55:17', '2018-02-05 15:55:15'), ('8', '2', '6', '2018-02-07 15:07:33', '2018-02-07 15:07:33'), ('10', '1', '5', '2018-02-07 16:59:40', '2018-02-07 16:59:40'), ('11', '2', '1', '2018-02-07 18:01:56', '2018-02-07 18:01:56'), ('13', '2', '2', '2018-03-12 15:28:37', '2018-03-12 15:28:37');
COMMIT;

-- ----------------------------
--  Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(300) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
--  Records of `sys_role`
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES ('1', '后台管理员', '2018-02-06 10:33:00', '2018-02-06 10:33:00'), ('2', '后台开发人员', '2018-02-06 15:25:40', '2018-02-06 15:25:40');
COMMIT;

-- ----------------------------
--  Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(80) DEFAULT NULL,
  `pwd` varchar(100) DEFAULT NULL,
  `role_ids` varchar(1000) DEFAULT NULL,
  `dept_id` int(11) DEFAULT NULL,
  `del_status` tinyint(2) DEFAULT '0' COMMENT 'DELETED: 1 , DEFAULT:0',
  `grade` int(2) DEFAULT '0' COMMENT '未分配权限',
  `action` int(1) DEFAULT '1' COMMENT '启用',
  `modify_date` datetime DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='系统用户';

-- ----------------------------
--  Records of `sys_user`
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES ('1', 'alice', '$2a$10$sWBIWVDAPt6Lt5ZTJOxfYeLW0xRK4TU48XxUuXmO4nsNo.rou9qxK', '[1,2]', '2', '0', '1', '1', '2018-04-12 14:39:23', '2018-04-12 14:39:23'), ('2', 'feng', '$2a$10$sWBIWVDAPt6Lt5ZTJOxfYeLW0xRK4TU48XxUuXmO4nsNo.rou9qxK', '[2]', '2', '0', '2', '1', '2018-04-12 14:49:17', '2018-04-12 14:49:17');
COMMIT;

-- ----------------------------
--  Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(64) DEFAULT NULL COMMENT '用户名称',
  `pwd` varchar(255) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL COMMENT '用户角色',
  `create_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `user`
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES ('1', 'alice', '123', '1', '2017-10-09 14:23:53', '2017-10-09 14:23:53');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
