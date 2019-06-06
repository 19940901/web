/*
Navicat MySQL Data Transfer

Source Server         : 192.168.26.128_3306
Source Server Version : 50173
Source Host           : 192.168.26.128:3306
Source Database       : saastest

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2018-07-19 10:44:07
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `shiro_user`
-- ----------------------------
DROP TABLE IF EXISTS `shiro_user`;
CREATE TABLE `shiro_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) NOT NULL,
  `password` varchar(50) NOT NULL,
  `locked` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0是未锁定1是锁定',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0是未删除，1是删除',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shiro_user
-- ----------------------------
INSERT INTO `shiro_user` VALUES ('1', 'root@shiro.com', 'b846ddbdcbebeadbc9745f19d9f19445', '0', '0');
INSERT INTO `shiro_user` VALUES ('2', 'admin@shiro.com', 'b846ddbdcbebeadbc9745f19d9f19445', '0', '0');
INSERT INTO `shiro_user` VALUES ('3', 'user@shiro.com', 'b846ddbdcbebeadbc9745f19d9f19445', '0', '0');
INSERT INTO `shiro_user` VALUES ('4', 'guest@shiro.com', 'b846ddbdcbebeadbc9745f19d9f19445', '0', '0');
