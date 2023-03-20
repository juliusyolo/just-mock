/*
 Navicat Premium Data Transfer

 Source Server         : julius_mysql
 Source Server Type    : MySQL
 Source Server Version : 80027 (8.0.27)
 Source Host           : 127.0.0.1:3306
 Source Schema         : just_mock

 Target Server Type    : MySQL
 Target Server Version : 80027 (8.0.27)
 File Encoding         : 65001

 Date: 20/03/2023 16:43:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for vm_instance_attach
-- ----------------------------
DROP TABLE IF EXISTS `vm_instance_attach`;
CREATE TABLE `vm_instance_attach` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实例名称',
  `pid` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '进程编号',
  `vendor` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '厂商',
  `platform` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '平台类型',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for vm_instance_mock_info
-- ----------------------------
DROP TABLE IF EXISTS `vm_instance_mock_info`;
CREATE TABLE `vm_instance_mock_info` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `pid` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '进程编号',
  `class_name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '类名',
  `method_name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '方法名',
  `method_args_desc` varchar(5000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '方法参数描述',
  `method_return_desc` varchar(5000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '方法返回值类型描述',
  `api_url` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'API URL',
  `api_type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'API类型',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

SET FOREIGN_KEY_CHECKS = 1;
