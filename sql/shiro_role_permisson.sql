/*
Navicat MySQL Data Transfer

Source Server         : 192.168.26.128_3306
Source Server Version : 50173
Source Host           : 192.168.26.128:3306
Source Database       : saastest

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2018-06-15 17:44:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `shiro_role_permisson`
-- ----------------------------
DROP TABLE IF EXISTS `shiro_role_permisson`;
CREATE TABLE `shiro_role_permisson` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `role_name` varchar(100) NOT NULL,
  `perm_id` int(11) NOT NULL,
  `perm_name` varchar(100) NOT NULL,
  `perm_perm` varchar(100) NOT NULL,
  PRIMARY KEY (`id`,`role_id`,`perm_id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shiro_role_permisson
-- ----------------------------
INSERT INTO `shiro_role_permisson` VALUES ('1', '2', '管理员', '1', '员工保存', 'employee:save');
INSERT INTO `shiro_role_permisson` VALUES ('2', '3', '用户', '2', '员工删除', 'employee:delete');
INSERT INTO `shiro_role_permisson` VALUES ('3', '4', '游客', '1', '员工保存', 'employee:save');
