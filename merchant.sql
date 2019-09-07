/*
SQLyog Ultimate v12.5.0 (64 bit)
MySQL - 8.0.15 : Database - merchant
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`merchant` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;

USE `merchant`;

/*Table structure for table `account` */

DROP TABLE IF EXISTS `account`;

CREATE TABLE `account` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账户id',
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `pwd` varchar(32) NOT NULL COMMENT '密码(md5加密)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_USERNAME` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='账户表';

/*Data for the table `account` */

insert  into `account`(`id`,`username`,`pwd`,`create_time`,`update_time`) values 
('1','wang','e10adc3949ba59abbe56e057f20f883e','2019-09-02 22:03:33','2019-09-02 22:03:35'),
('2','zhang','e10adc3949ba59abbe56e057f20f883e','2019-09-07 20:48:37','2019-09-07 20:48:39'),
('3','li','e10adc3949ba59abbe56e057f20f883e','2019-09-07 20:49:18','2019-09-07 20:49:20');

/*Table structure for table `account_brand_link` */

DROP TABLE IF EXISTS `account_brand_link`;

CREATE TABLE `account_brand_link` (
  `id` varchar(32) NOT NULL,
  `account_id` varchar(32) NOT NULL COMMENT '账户id(关联account.id)',
  `merchant_id` varchar(32) NOT NULL COMMENT '商家id',
  `brand_id` varchar(32) NOT NULL COMMENT '品牌id(关联brand.id)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='账户品牌关联表';

/*Data for the table `account_brand_link` */

insert  into `account_brand_link`(`id`,`account_id`,`merchant_id`,`brand_id`,`create_time`,`update_time`) values 
('1','2','1','1','2019-09-07 20:50:48','2019-09-07 20:50:50'),
('2','3','1','2','2019-09-07 20:50:59','2019-09-07 20:51:01');

/*Table structure for table `account_merchant_link` */

DROP TABLE IF EXISTS `account_merchant_link`;

CREATE TABLE `account_merchant_link` (
  `id` varchar(32) NOT NULL,
  `account_id` varchar(32) NOT NULL COMMENT '账户id(关联account.id)',
  `merchant_id` varchar(32) NOT NULL COMMENT '商家id(关联merchant.id)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `IDX_ACCOUNT_ID` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='账户商家关联表';

/*Data for the table `account_merchant_link` */

insert  into `account_merchant_link`(`id`,`account_id`,`merchant_id`,`create_time`,`update_time`) values 
('1','1','1','2019-09-07 14:56:22','2019-09-07 14:56:24');

/*Table structure for table `brand` */

DROP TABLE IF EXISTS `brand`;

CREATE TABLE `brand` (
  `id` varchar(32) NOT NULL COMMENT '品牌id',
  `merchant_id` varchar(32) NOT NULL COMMENT '商家id(关联merchant.id)',
  `name` varchar(20) NOT NULL COMMENT '品牌名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `IDX_MERCHANT_ID` (`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='品牌信息表';

/*Data for the table `brand` */

insert  into `brand`(`id`,`merchant_id`,`name`,`create_time`,`update_time`) values 
('1','1','抖音','2019-09-07 14:44:11','2019-09-07 14:44:13'),
('2','1','火山小视频','2019-09-07 14:45:07','2019-09-07 14:45:09');

/*Table structure for table `merchant` */

DROP TABLE IF EXISTS `merchant`;

CREATE TABLE `merchant` (
  `id` varchar(32) NOT NULL COMMENT '商家id',
  `name` varchar(20) NOT NULL COMMENT '商家名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商家信息表';

/*Data for the table `merchant` */

insert  into `merchant`(`id`,`name`,`create_time`,`update_time`) values 
('1','字节跳动','2019-09-07 14:43:39','2019-09-07 14:43:41');

/*Table structure for table `product` */

DROP TABLE IF EXISTS `product`;

CREATE TABLE `product` (
  `id` varchar(32) NOT NULL COMMENT '商品id',
  `merchant_id` varchar(32) NOT NULL COMMENT '商家id(关联merchant.id)',
  `brand_id` varchar(32) NOT NULL COMMENT '品牌id(关联brand.id)',
  `name` varchar(20) NOT NULL COMMENT '商品名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `IDX_MERCHANT_iD` (`merchant_id`),
  KEY `IDX_BRAND_ID` (`brand_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品表';

/*Data for the table `product` */

insert  into `product`(`id`,`merchant_id`,`brand_id`,`name`,`create_time`,`update_time`) values 
('1','1','1','抖音一姐','2019-09-07 14:45:36','2019-09-07 14:45:39'),
('2','1','1','抖音一哥','2019-09-07 14:45:50','2019-09-07 14:45:53'),
('3','1','2','火山一姐','2019-09-07 14:46:06','2019-09-07 14:46:09');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
