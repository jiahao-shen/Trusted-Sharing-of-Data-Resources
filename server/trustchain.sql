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

DROP TABLE IF EXISTS `organization_register`;
CREATE TABLE `organization_register`
(
    `serial_number` bigint             not null,
    `id`            bigint,
    `name`          varchar(32) unique not null,
    `logo`          varchar(1024),
    `type`          int                not null,
    `telephone`     varchar(32)        not null,
    `email`         varchar(32)        not null,
    `city`          varchar(128)       not null,
    `address`       varchar(128)       not null,
    `introduction`  varchar(1024),
    `superior`      bigint,
    `provide_node`  bool               not null,
    `num_nodes`     int                not null,
    `file`          varchar(1024),
    `status`        int                not null,
    `apply_time`    datetime           not null,
    `reply_time`    datetime,
    `reply_message` varchar(1024),
    PRIMARY KEY (`serial_number`),
    FOREIGN KEY (`id`) REFERENCES `organization` (`id`)
) DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization`
(
    `id`           bigint             not null,
    `name`         varchar(32) unique not null,
    `logo`         varchar(1024),
    `type`         int                not null,
    `telephone`    varchar(32)        not null,
    `email`        varchar(32)        not null,
    `city`         varchar(128)       not null,
    `address`      varchar(128)       not null,
    `introduction` varchar(1024),
    `superior`     bigint,
    `provide_node` bool               not null,
    `num_nodes`    int                not null,
    `file`         varchar(1024),
    `created_time` datetime           not null,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`superior`) REFERENCES `organization` (`id`)
) DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`           bigint             not null,
    `username`     varchar(32) unique not null,
    `password`     varchar(128)       not null,
    `organization` bigint             not null,
    `created_time` datetime           not null,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`organization`) REFERENCES `organization` (`id`)
) DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `api_register`;
CREATE TABLE `api_register`
(
    `serial_number` bigint        not null,
    `id`            bigint,
    `author`        bigint        not null,
    `organization`  bigint        not null,
    `name`          varchar(32)   not null,
    `url`           varchar(1024) not null,
    `method`        int           not null,
    `introduction`  varchar(1024),
    `category`      int,
    `authorize`     varchar(32),
    `version`       varchar(32)   not null,
    `header_type`   int,
    `header`        varchar(1024),
    `request_type`  int,
    `request`       varchar(2014),
    `response_type` int,
    `response`      varchar(1024),
    `status`        int           not null,
    `apply_time`    datetime      not null,
    `reply_time`    datetime,
    `reply_message` varchar(1024),
    PRIMARY KEY (`serial_number`),
    FOREIGN KEY (`id`) REFERENCES `api` (`id`)
) DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `api`;
CREATE TABLE `api`
(
    `id`            bigint,
    `author`        bigint        not null,
    `organization`  bigint        not null,
    `name`          varchar(32)   not null,
    `url`           varchar(1024) not null,
    `method`        int           not null,
    `introduction`  varchar(1024),
    `category`      int,
    `authorize`     varchar(32),
    `version`       varchar(32)   not null,
    `header_type`   int,
    `header`        varchar(1024),
    `request_type`  int,
    `request`       varchar(2014),
    `response_type` int,
    `response`      varchar(1024),
    `created_time`  datetime      not null,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`author`) REFERENCES `user` (`id`),
    FOREIGN KEY (`organization`) REFERENCES `organization` (`id`)
) DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `api_invoke`;
CREATE TABLE `api_invoke`
(
    `serial_number` bigint   not null,
    `id`            bigint   not null,
    `applicant`     bigint   not null,
    `author`        bigint   not null,
    `invoke_method` int      not null,
    `start_time`    datetime not null,
    `end_time`      datetime not null,
    `status`        int      not null,
    `comment`       varchar(512),
    `apply_time`    datetime not null,
    `reply_time`    datetime,
    `reply_message` varchar(1024),
    PRIMARY KEY (`serial_number`),
    FOREIGN KEY (`id`) REFERENCES `api` (`id`),
    FOREIGN KEY (`applicant`) REFERENCES `user` (`id`),
    FOREIGN KEY (`author`) REFERENCES `user` (`id`)
) DEFAULT CHARSET = utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

