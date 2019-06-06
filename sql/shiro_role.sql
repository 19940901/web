/*
Navicat MySQL Data Transfer

Source Server         : 192.168.26.128_3306
Source Server Version : 50173
Source Host           : 192.168.26.128:3306
Source Database       : saastest

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2018-06-15 17:44:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `shiro_role`
-- ----------------------------
DROP TABLE IF EXISTS `shiro_role`;
CREATE TABLE `shiro_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `role` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shiro_role
-- ----------------------------
INSERT INTO `shiro_role` VALUES ('1', '超级管理员', 'root');
INSERT INTO `shiro_role` VALUES ('2', '管理员', 'admin');
INSERT INTO `shiro_role` VALUES ('3', '用户', 'user');
INSERT INTO `shiro_role` VALUES ('4', '游客', 'guest');
