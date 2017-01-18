/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  rahulkk
 * Created: 18 Jan, 2017
 */

ALTER TABLE `analytics_data` 
ADD COLUMN `image_filename` VARCHAR(255) NULL DEFAULT NULL AFTER `last_updated_by`;
