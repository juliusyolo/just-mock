/*
 Navicat Premium Data Transfer

 Source Server         : flowable_3308
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : just_mock

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 01/04/2023 21:41:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for mock_template_info
-- ----------------------------
DROP TABLE IF EXISTS `mock_template_info`;
CREATE TABLE `mock_template_info`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `template_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '模板内容',
  `el` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'el表达式',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标签',
  `random_variables` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '随即变量',
  `task_definitions` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '任务定义',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of mock_template_info
-- ----------------------------
INSERT INTO `mock_template_info` VALUES (3, '{\n     \"age\":\"${p0.name}\",\n     \"name\":\"${name!}\"\n}', NULL, 'Julius is genius;all', '[{\"name\":\"name\",\"sequence\":\"tom,julius,jack,kitty\"}]', '[\"{\\n  \\\"type\\\": \\\"HTTP_TASK\\\",\\n  \\\"content\\\": {\\n    \\\"url\\\": \\\"http://127.0.0.1:9090/hello1\\\",\\n    \\\"payloadType\\\": \\\"application/json\\\",\\n    \\\"payload\\\": \\\"{\\\\\\\"age\\\\\\\":\\\\\\\"${p0.name}\\\\\\\",\\\\\\\"name\\\\\\\":\\\\\\\"${name!}\\\\\\\"}\\\",\\n    \\\"method\\\": \\\"POST\\\"\\n  }\\n}\",\"{\\n  \\\"type\\\": \\\"HTTP_TASK\\\",\\n  \\\"content\\\": {\\n    \\\"url\\\": \\\"http://127.0.0.1:9090/hello2\\\",\\n    \\\"payloadType\\\": \\\"application/json\\\",\\n    \\\"payload\\\": \\\"age=${p0.name}&name=${name}\\\",\\n    \\\"method\\\": \\\"GET\\\"\\n  }\\n}\",\"{\\n  \\\"type\\\": \\\"HTTP_TASK\\\",\\n  \\\"content\\\": {\\n    \\\"url\\\": \\\"http://localhost:9090/hello3\\\",\\n    \\\"payloadType\\\": \\\"application/x-www-form-urlencoded\\\",\\n    \\\"payload\\\": \\\"age=${p0.name}&name=${name}\\\",\\n    \\\"method\\\": \\\"POST\\\"\\n  }\\n}\"]', '2023-03-31 19:08:20', '2023-04-01 20:54:58');
INSERT INTO `mock_template_info` VALUES (6, '{\n     \"age\":\"${p0.name}\",\n     \"name\":\"${name!}\"\n}', NULL, 'GET;query', '[{\"name\":\"name\",\"sequence\":\"000000,999999\"}]', '[\"{\\n  \\\"type\\\": \\\"HTTP_TASK\\\",\\n  \\\"content\\\": {\\n    \\\"url\\\": \\\"http://127.0.0.1:9090/hello2\\\",\\n    \\\"payloadType\\\": \\\"application/json\\\",\\n    \\\"payload\\\": \\\"age=${p0.name}&name=${name}\\\",\\n    \\\"method\\\": \\\"GET\\\"\\n  }\\n}\"]', '2023-04-01 04:37:09', '2023-04-01 19:55:35');
INSERT INTO `mock_template_info` VALUES (7, '{\n     \"age\":\"${p0.name}\",\n     \"name\":\"${name!}\"\n}', NULL, 'POST;form-data', '[{\"name\":\"name\",\"sequence\":\"julius,sdefaa\"}]', '[\"{\\n  \\\"type\\\": \\\"HTTP_TASK\\\",\\n  \\\"content\\\": {\\n    \\\"url\\\": \\\"http://localhost:9090/hello3\\\",\\n    \\\"payloadType\\\": \\\"application/x-www-form-urlencoded\\\",\\n    \\\"payload\\\": \\\"age=${p0.name}&name=${name}\\\",\\n    \\\"method\\\": \\\"POST\\\"\\n  }\\n}\"]', '2023-04-01 19:14:22', '2023-04-01 19:23:22');
INSERT INTO `mock_template_info` VALUES (8, '{\n     \"age\":\"${p0.name}\",\n     \"name\":\"${name!}\"\n}', NULL, 'POST;json', '[{\"name\":\"name\",\"sequence\":\"julius,hello\"}]', '[\"{\\n  \\\"type\\\": \\\"HTTP_TASK\\\",\\n  \\\"content\\\": {\\n    \\\"url\\\": \\\"http://127.0.0.1:9090/hello1\\\",\\n    \\\"payloadType\\\": \\\"application/json\\\",\\n    \\\"payload\\\": \\\"{\\\\\\\"age\\\\\\\":\\\\\\\"${p0.name}\\\\\\\",\\\\\\\"name\\\\\\\":\\\\\\\"${name!}\\\\\\\"}\\\",\\n    \\\"method\\\": \\\"POST\\\"\\n  }\\n}\"]', '2023-04-01 19:34:49', '2023-04-01 19:35:34');

-- ----------------------------
-- Table structure for vm_instance_attach_extra_info
-- ----------------------------
DROP TABLE IF EXISTS `vm_instance_attach_extra_info`;
CREATE TABLE `vm_instance_attach_extra_info`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `pid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '进程号',
  `port` int NULL DEFAULT NULL COMMENT '交互端口',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of vm_instance_attach_extra_info
-- ----------------------------

-- ----------------------------
-- Table structure for vm_instance_attach_info
-- ----------------------------
DROP TABLE IF EXISTS `vm_instance_attach_info`;
CREATE TABLE `vm_instance_attach_info`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '实例名称',
  `pid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '进程编号',
  `vendor` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '厂商',
  `platform` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '平台类型',
  `environment_variables` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '环境变量',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of vm_instance_attach_info
-- ----------------------------

-- ----------------------------
-- Table structure for vm_instance_mock_info
-- ----------------------------
DROP TABLE IF EXISTS `vm_instance_mock_info`;
CREATE TABLE `vm_instance_mock_info`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `pid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '进程编号',
  `class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '类名',
  `method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '方法名',
  `method_args_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '方法参数描述',
  `method_return_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '方法返回值类型描述',
  `class_annotations_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '类注解',
  `method_annotations_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '方法注解',
  `api_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'API 方法',
  `api_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'API URL',
  `api_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'API类型',
  `mock_enable` tinyint(1) NULL DEFAULT NULL COMMENT '是否开启mock',
  `mock_template_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '模板编号',
  `mock_template_snapshot` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT 'mock模板快照',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of vm_instance_mock_info
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
