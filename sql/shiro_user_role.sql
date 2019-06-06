/*
Navicat MySQL Data Transfer

Source Server         : 192.168.26.128_3306
Source Server Version : 50173
Source Host           : 192.168.26.128:3306
Source Database       : saastest

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2018-06-15 17:44:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `shiro_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `shiro_user_role`;
CREATE TABLE `shiro_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `user_name` varchar(100) NOT NULL,
  `role_id` int(11) NOT NULL,
  `role_name` varchar(100) NOT NULL,
  `role_role` varchar(100) NOT NULL,
  PRIMARY KEY (`id`,`role_id`,`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shiro_user_role
-- ----------------------------
INSERT INTO `shiro_user_role` VALUES ('1', '2', 'admin@shiro.com', '2', '管理员', 'admin');
INSERT INTO `shiro_user_role` VALUES ('2', '2', 'admin@shiro.com', '3', '用户', 'user');
INSERT INTO `shiro_user_role` VALUES ('3', '2', 'admin@shiro.com', '4', '游客', 'guest');
INSERT INTO `shiro_user_role` VALUES ('4', '4', 'guest@shiro.com', '4', '游客', 'guest');
