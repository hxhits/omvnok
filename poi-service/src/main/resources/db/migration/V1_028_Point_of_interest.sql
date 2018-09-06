ALTER TABLE `omart_db`.`omart_point_of_interest` 
ADD COLUMN `poi_type` INT(1) NULL DEFAULT 0 COMMENT '0: Shop\n1: Business' AFTER `display_state`,
ADD COLUMN `fax` VARCHAR(45) NULL DEFAULT '' AFTER `web_address`,
ADD COLUMN `tel` VARCHAR(45) NULL DEFAULT '' AFTER `fax`,
ADD COLUMN `tax` VARCHAR(45) NULL DEFAULT '' AFTER `tel`,
ADD COLUMN `facebook` VARCHAR(255) NULL DEFAULT '' AFTER `tax`,
ADD COLUMN `twitter` VARCHAR(255) NULL DEFAULT '' AFTER `facebook`;
