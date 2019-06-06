/*
Navicat MySQL Data Transfer

Source Server         : 192.168.26.128_3306
Source Server Version : 50173
Source Host           : 192.168.26.128:3306
Source Database       : saastest

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2018-06-15 17:44:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `shiro_permission`
-- ----------------------------
DROP TABLE IF EXISTS `shiro_permission`;
CREATE TABLE `shiro_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `perm` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shiro_permission
-- ----------------------------
INSERT INTO `shiro_permission` VALUES ('1', '员工保存', 'employee:save');
INSERT INTO `shiro_permission` VALUES ('2', '员工删除', 'employee:delete');
INSERT INTO `shiro_permission` VALUES ('3', '员工查看', 'employee:get');
