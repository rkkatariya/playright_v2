-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: playright
-- ------------------------------------------------------
-- Server version	5.7.10-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `analytics_data`
--

DROP TABLE IF EXISTS `analytics_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analytics_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `news_date` timestamp NULL DEFAULT NULL,
  `news_paper` varchar(45) DEFAULT NULL,
  `language` varchar(45) DEFAULT NULL,
  `headline` varchar(45) DEFAULT NULL,
  `edition` varchar(45) DEFAULT NULL,
  `supplement` varchar(45) DEFAULT NULL,
  `source` varchar(45) DEFAULT NULL,
  `page_no` int(11) DEFAULT NULL,
  `height` int(11) DEFAULT NULL,
  `width` int(11) DEFAULT NULL,
  `total_article_size` int(11) DEFAULT NULL,
  `circulation_figure` int(11) DEFAULT NULL,
  `quantitative_AVE` int(11) DEFAULT NULL,
  `journalist_factor` int(11) DEFAULT NULL,
  `image` mediumblob,
  `image_exists` varchar(45) DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `last_updated_by` int(11) DEFAULT NULL,
  `file_size` int(11) DEFAULT NULL,
  `file_type` varchar(45) DEFAULT NULL,
  `active` int(11) DEFAULT '1',
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


DROP TABLE IF EXISTS `sb_companies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sb_companies` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `active` int(1) NOT NULL DEFAULT '1',
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int(11) NOT NULL DEFAULT '0',
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` int(11) NOT NULL DEFAULT '0',
  `description` varchar(4000) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `business_owner` int(11) DEFAULT '0',
  `comments` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sb_entitlements`
--

DROP TABLE IF EXISTS `sb_entitlements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sb_entitlements` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `resource` varchar(255) NOT NULL,
  `action` varchar(255) NOT NULL,
  `active` int(1) NOT NULL DEFAULT '1',
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int(11) NOT NULL DEFAULT '0',
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` int(11) NOT NULL DEFAULT '0',
  `description` varchar(4000) DEFAULT NULL,
  `comments` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `sb_location`
--

DROP TABLE IF EXISTS `sb_location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sb_location` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `active` int(1) NOT NULL DEFAULT '1',
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int(11) NOT NULL DEFAULT '0',
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` int(11) NOT NULL DEFAULT '0',
  `description` varchar(4000) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `business_owner` int(11) DEFAULT '0',
  `comments` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_location_project_idx` (`project`),
  CONSTRAINT `fk_location_project` FOREIGN KEY (`project`) REFERENCES `sb_projects` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sb_lov`
--

DROP TABLE IF EXISTS `sb_lov`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sb_lov` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company` int(11) DEFAULT NULL,
  `project` int(11) DEFAULT NULL,
  `location` int(11) DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  `active` int(1) NOT NULL DEFAULT '1',
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int(11) NOT NULL DEFAULT '0',
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` int(11) NOT NULL DEFAULT '0',
  `comments` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_lov_project_idx` (`project`),
  KEY `fk_lov_location_idx` (`location`),
  KEY `fk_lov_company_idx` (`company`),
  CONSTRAINT `fk_lov_location` FOREIGN KEY (`location`) REFERENCES `sb_location` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_lov_project` FOREIGN KEY (`project`) REFERENCES `sb_projects` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sb_lov_attr`
--

DROP TABLE IF EXISTS `sb_lov_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sb_lov_attr` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lov` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  `active` int(1) NOT NULL DEFAULT '1',
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int(11) NOT NULL DEFAULT '0',
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` int(11) NOT NULL DEFAULT '0',
  `comments` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_lov_type_lov_idx` (`lov`),
  CONSTRAINT `fk_lov_attr_lov` FOREIGN KEY (`lov`) REFERENCES `sb_lov` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sb_projects`
--

DROP TABLE IF EXISTS `sb_projects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sb_projects` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(4000) DEFAULT NULL,
  `coordinates` varchar(255) DEFAULT NULL,
  `active` int(1) NOT NULL DEFAULT '1',
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int(11) NOT NULL DEFAULT '0',
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` int(11) NOT NULL DEFAULT '0',
  `comments` varchar(2000) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `business_owner` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_project_company_idx` (`company`),
  CONSTRAINT `fk_project_company` FOREIGN KEY (`company`) REFERENCES `sb_companies` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sb_role_ent_mapping`
--

DROP TABLE IF EXISTS `sb_role_ent_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sb_role_ent_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role` int(11) NOT NULL,
  `role_name` varchar(255) DEFAULT NULL,
  `entitlement` int(11) NOT NULL,
  `entitlement_name` varchar(255) DEFAULT NULL,
  `context` varchar(255) NOT NULL,
  `active` int(1) NOT NULL DEFAULT '1',
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int(11) NOT NULL DEFAULT '0',
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` int(11) NOT NULL DEFAULT '0',
  `description` varchar(4000) DEFAULT NULL,
  `comments` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_rem_role_idx` (`role`),
  KEY `fk_rem_entitlement_idx` (`entitlement`),
  CONSTRAINT `fk_rem_entitlement` FOREIGN KEY (`entitlement`) REFERENCES `sb_entitlements` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_rem_role` FOREIGN KEY (`role`) REFERENCES `sb_roles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=174 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sb_roles`
--

DROP TABLE IF EXISTS `sb_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sb_roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `raw_name` varchar(100) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(4000) DEFAULT NULL,
  `active` int(1) NOT NULL DEFAULT '1',
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int(11) NOT NULL DEFAULT '0',
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` int(11) NOT NULL DEFAULT '0',
  `comments` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sb_roles_context`
--

DROP TABLE IF EXISTS `sb_roles_context`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sb_roles_context` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_ent` int(11) NOT NULL,
  `allowed_role` int(11) NOT NULL,
  `active` int(1) NOT NULL DEFAULT '1',
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int(11) NOT NULL DEFAULT '0',
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` int(11) NOT NULL DEFAULT '0',
  `description` varchar(4000) DEFAULT NULL,
  `comments` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_role_ctx_rem_idx` (`role_ent`),
  KEY `fk_role_ctx_role_idx` (`allowed_role`),
  CONSTRAINT `fk_role_ctx_rem` FOREIGN KEY (`role_ent`) REFERENCES `sb_role_ent_mapping` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_role_ctx_role` FOREIGN KEY (`allowed_role`) REFERENCES `sb_roles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sb_settings`
--

DROP TABLE IF EXISTS `sb_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sb_settings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company` int(11) DEFAULT NULL,
  `project` int(11) DEFAULT NULL,
  `location` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parameter` varchar(255) NOT NULL,
  `value` varchar(1000) DEFAULT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `active` int(1) NOT NULL DEFAULT '1',
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int(11) NOT NULL DEFAULT '0',
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` int(11) NOT NULL DEFAULT '0',
  `comments` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_setting_project_idx` (`project`),
  KEY `fk_setting_location_idx` (`location`),
  KEY `fk_setting_company_idx` (`company`),
  CONSTRAINT `fk_setting_company` FOREIGN KEY (`company`) REFERENCES `sb_companies` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_setting_location` FOREIGN KEY (`location`) REFERENCES `sb_location` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_setting_project` FOREIGN KEY (`project`) REFERENCES `sb_projects` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sb_user_proj_loc_mapping`
--

DROP TABLE IF EXISTS `sb_user_proj_loc_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sb_user_proj_loc_mapping` (
  `project` int(11) DEFAULT NULL,
  `location` int(11) DEFAULT NULL,
  `user` int(11) NOT NULL,
  `active` int(1) NOT NULL DEFAULT '1',
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int(11) NOT NULL DEFAULT '0',
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` int(11) NOT NULL DEFAULT '0',
  `comments` varchar(2000) DEFAULT NULL,
  KEY `fk_upl_map_user_idx` (`user`),
  KEY `fk_upl_map_loc_idx` (`location`),
  KEY `fk_upl_map_proj_idx` (`project`),
  CONSTRAINT `fk_upl_map_loc` FOREIGN KEY (`location`) REFERENCES `sb_location` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upl_map_proj` FOREIGN KEY (`project`) REFERENCES `sb_projects` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upl_map_user` FOREIGN KEY (`user`) REFERENCES `sb_users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sb_user_role_mapping`
--

DROP TABLE IF EXISTS `sb_user_role_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sb_user_role_mapping` (
  `user` int(11) NOT NULL,
  `role` int(11) NOT NULL,
  `active` int(1) NOT NULL DEFAULT '1',
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int(11) NOT NULL DEFAULT '0',
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` int(11) NOT NULL DEFAULT '0',
  `comments` varchar(2000) DEFAULT NULL,
  KEY `fk_ur_map_user_idx` (`user`),
  KEY `fk_ur_map_role_idx` (`role`),
  CONSTRAINT `fk_ur_map_role` FOREIGN KEY (`role`) REFERENCES `sb_roles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ur_map_user` FOREIGN KEY (`user`) REFERENCES `sb_users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sb_users`
--

DROP TABLE IF EXISTS `sb_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sb_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `email_address` varchar(255) NOT NULL,
  `mobile_no` varchar(25) DEFAULT NULL,
  `alt_phone_no` varchar(25) DEFAULT NULL,
  `password` varchar(45) NOT NULL,
  `reset_password` int(11) NOT NULL DEFAULT '0',
  `manager` int(11) DEFAULT NULL,
  `photo` blob,
  `gender` varchar(20) DEFAULT NULL,
  `last_login` timestamp NULL DEFAULT NULL,
  `active` int(1) NOT NULL DEFAULT '1',
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int(11) NOT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` int(11) NOT NULL,
  `comments` varchar(2000) DEFAULT NULL,
  `last_login_from` varchar(255) DEFAULT NULL,
  `job_title` varchar(256) DEFAULT NULL,
  `department` varchar(256) DEFAULT NULL,
  `salt` varchar(32) DEFAULT NULL,
  `company` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_UNIQUE` (`username`),
  UNIQUE KEY `email_adddress_UNIQUE` (`email_address`),
  UNIQUE KEY `mobile_no_UNIQUE` (`mobile_no`),
  KEY `fk_users_created_by_idx` (`created_by`),
  KEY `fk_users_last_updated_by_idx` (`last_updated_by`),
  KEY `fk_users_manager_idx` (`manager`),
  KEY `fk_users_company_idx` (`company`),
  CONSTRAINT `fk_users_company` FOREIGN KEY (`company`) REFERENCES `sb_companies` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_created_by` FOREIGN KEY (`created_by`) REFERENCES `sb_users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_last_updated_by` FOREIGN KEY (`last_updated_by`) REFERENCES `sb_users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_manager` FOREIGN KEY (`manager`) REFERENCES `sb_users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-01-16 17:47:36

DROP TABLE IF EXISTS `sb_email_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sb_email_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company` int(11) NOT NULL,
  `project` int(11) DEFAULT NULL,
  `location` int(11) DEFAULT NULL,
  `requested_by` int(11) DEFAULT NULL,
  `from_id` int(11) DEFAULT NULL,
  `to_id` int(11) DEFAULT NULL,
  `source` varchar(255) NOT NULL,
  `status` varchar(45) NOT NULL,
  `from` varchar(255) NOT NULL,
  `to` varchar(1000) NOT NULL,
  `cc` varchar(1000) DEFAULT NULL,
  `bcc` varchar(1000) DEFAULT NULL,
  `template` int(11) DEFAULT NULL,
  `subject` varchar(1000) DEFAULT NULL,
  `body` varchar(4000) DEFAULT NULL,
  `sent_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `attachments` int(1) DEFAULT '0',
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int(11) NOT NULL DEFAULT '0',
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` int(11) NOT NULL DEFAULT '0',
  `comments` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_email_log_from_idx` (`from_id`),
  KEY `fk_email_log_from_idx1` (`to_id`),
  KEY `fk_email_log_requested_by_idx` (`requested_by`),
  CONSTRAINT `fk_email_log_from` FOREIGN KEY (`from_id`) REFERENCES `sb_users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_email_log_requested_by` FOREIGN KEY (`requested_by`) REFERENCES `sb_users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;