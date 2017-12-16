/*
 数据结构+简单的测试数据
 Navicat Premium Data Transfer

 Source Server         : 172.28.1.3 内部DEV
 Source Server Type    : MySQL
 Source Server Version : 50621
 Source Host           : 172.28.1.3
 Source Database       : act_test

 Target Server Type    : MySQL
 Target Server Version : 50621
 File Encoding         : utf-8

 Date: 12/13/2017 16:32:37 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `item`
-- ----------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `desc` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `item`
-- ----------------------------
BEGIN;
INSERT INTO `item` VALUES ('1', 'dfdff');
COMMIT;

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='功能点及菜单列表';

-- ----------------------------
--  Records of `sys_func`
-- ----------------------------
BEGIN;
INSERT INTO `sys_func` VALUES ('1', '0', '用户管理', null, '0', '1', '1', '1', '2017-12-04 16:41:24', '2017-12-04 16:41:27'), ('2', '1', '用户列表', '/sys/user/list', '1', '1', '1', '2', '2017-12-04 10:48:18', '2017-12-04 10:48:22'), ('3', '1', '增加用户', '/sys/user/add', '1', '0', '1', '3', '2017-12-04 10:48:56', '2017-12-04 10:48:59'), ('4', '1', '编辑用户资料', '/sys/user/edit/*', '1', '0', '1', '4', '2017-12-04 10:49:38', '2017-12-04 10:49:41');
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='权限表';

-- ----------------------------
--  Records of `sys_permission`
-- ----------------------------
BEGIN;
INSERT INTO `sys_permission` VALUES ('1', '1', '1', '2017-12-04 11:24:17', '2017-12-04 11:24:19'), ('2', '1', '2', '2017-12-04 11:25:03', '2017-12-04 11:25:04'), ('3', '1', '3', '2017-12-04 11:25:16', '2017-12-04 11:25:18'), ('4', '1', '4', '2017-12-04 17:15:25', '2017-12-04 17:15:26');
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
--  Records of `sys_role`
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES ('1', '后台管理员', '2017-12-04 11:23:36', '2017-12-04 11:23:37');
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
  `del_status` tinyint(2) DEFAULT '0' COMMENT 'DELETED: -1 , DEFAULT:0',
  `modify_date` datetime DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='系统用户';

-- ----------------------------
--  Records of `sys_user`
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES ('1', 'alice', '111', '[1]', '1', '0', '2017-12-04 10:47:21', '2017-12-04 10:47:23');
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `user`
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES ('1', 'alice', '123', '1', '2017-10-09 14:23:53', '2017-10-09 14:23:53'), ('5', 'alice', '123', '1', '2017-10-11 14:46:30', '2017-10-11 14:46:30'), ('6', 'alice', '123', '1', '2017-11-24 18:27:27', '2017-11-24 18:27:27');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
