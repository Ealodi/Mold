/*
 Navicat Premium Dump SQL

 Source Server         : mysql84
 Source Server Type    : MySQL
 Source Server Version : 80406 (8.4.6)
 Source Host           : localhost:3307
 Source Schema         : mold

 Target Server Type    : MySQL
 Target Server Version : 80406 (8.4.6)
 File Encoding         : 65001

 Date: 20/08/2025 18:44:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for client
-- ----------------------------
DROP TABLE IF EXISTS `client`;
CREATE TABLE `client`  (
                           `clientID` int NOT NULL AUTO_INCREMENT,
                           `clientName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                           `phoneNumber` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                           `address` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                           `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                           `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除标志：0=未删除，1=已删除',
                           PRIMARY KEY (`clientID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
                           `orderID` int NOT NULL AUTO_INCREMENT,
                           `clientID` int NULL DEFAULT NULL,
                           `partNum` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                           `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                           `num` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                           `nameArticle` enum('模仁','电极','滑块','镶件','配件') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                           `process` enum('CNC','放电','线切割','全加工') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                           `unitPrice` decimal(10, 2) NOT NULL,
                           `workPieceNum` int NOT NULL,
                           `moldType` enum('修模','新模') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                           `totalAmount` decimal(10, 2) GENERATED ALWAYS AS ((`unitPrice` * `workPieceNum`)) STORED NULL,
                           `paymentStatus` enum('未付款','已付款') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '未付款',
                           `payWay` enum('现金','承兑汇票') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                           `status` enum('进行中','未开始','完成') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '未开始',
                           `completeTime` timestamp NULL DEFAULT NULL,
                           `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除标志：0=未删除，1=已删除',
                           `clientName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户名称',
                           PRIMARY KEY (`orderID`) USING BTREE,
                           INDEX `clientID`(`clientID` ASC) USING BTREE,
                           CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`clientID`) REFERENCES `client` (`clientID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
