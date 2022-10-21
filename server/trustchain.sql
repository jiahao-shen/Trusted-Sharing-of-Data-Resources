-- MariaDB dump 10.19  Distrib 10.8.3-MariaDB, for osx10.17 (x86_64)
--
-- Host: localhost    Database: trustchain
-- ------------------------------------------------------
-- Server version	10.8.3-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

CREATE TABLE `register_organization`
(
    `serial_number` bigint       not null,
    `name`          varchar(32)  not null,
    `logo`          varchar(1024),
    `type`          int          not null,
    `telephone`     varchar(32)  not null,
    `email`         varchar(32)  not null,
    `city`          varchar(128) not null,
    `address`       varchar(128) not null,
    `introduction`  varchar(1024),
    `superior`      bigint,
    `provide_node`  bool         not null,
    `num_nodes`     int          not null,
    `file`          varchar(1024),
    `status`        int          not null,
    `apply_time`    datetime     not null,
    `reply_time`    datetime,
    `reply_message` varchar(1024),
    primary key (`serial_number`)
) DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization`
(
    `id`           bigint       not null,
    `name`         varchar(32)  not null,
    `logo`         varchar(1024),
    `type`         int          not null,
    `telephone`    varchar(32)  not null,
    `email`        varchar(32)  not null,
    `city`         varchar(128) not null,
    `address`      varchar(128) not null,
    `introduction` varchar(1024),
    `superior`     bigint,
    `provide_node` bool         not null,
    `num_nodes`    int          not null,
    `file`         varchar(1024),
    `created_time` datetime     not null,
    primary key (`id`)
) DEFAULT CHARSET = utf8mb4;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user`
(
    `username`  varchar(32)  NOT NULL,
    `password`  varchar(128) NOT NULL,
    `image_url` varchar(1024) DEFAULT NULL,
    `fabric_id` varchar(64)  NOT NULL,
    PRIMARY KEY (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

