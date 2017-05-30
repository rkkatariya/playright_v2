/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  rahulkk
 * Created: 14 Feb, 2017
 */

ALTER TABLE `sb_email_log` 
CHANGE COLUMN `body` `body` MEDIUMTEXT NULL DEFAULT NULL ;

ALTER TABLE `analytics_data`
CHANGE COLUMN `quantitative_AVE` `customer` VARCHAR(45) NULL ;

ALTER TABLE `analytics_data` 
CHANGE COLUMN `news_paper` `news_paper` VARCHAR(255) NULL DEFAULT NULL ,
CHANGE COLUMN `language` `language` VARCHAR(255) NULL DEFAULT NULL ,
CHANGE COLUMN `headline` `headline` VARCHAR(255) NULL DEFAULT NULL ,
CHANGE COLUMN `edition` `edition` VARCHAR(255) NULL DEFAULT NULL ,
CHANGE COLUMN `supplement` `supplement` VARCHAR(255) NULL DEFAULT NULL ,
CHANGE COLUMN `source` `source` VARCHAR(255) NULL DEFAULT NULL ,
CHANGE COLUMN `customer` `customer` VARCHAR(255) NULL DEFAULT NULL ,
CHANGE COLUMN `image_exists` `image_exists` VARCHAR(8) NULL DEFAULT NULL ,
CHANGE COLUMN `file_type` `file_type` VARCHAR(255) NULL DEFAULT NULL ;
