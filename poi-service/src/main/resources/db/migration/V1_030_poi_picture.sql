ALTER TABLE `omart_db`.`omart_poi_picture` 
ADD COLUMN `description` TEXT CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL AFTER `created_at`,
ADD COLUMN `latitude` DOUBLE NULL DEFAULT 0 AFTER `description`,
ADD COLUMN `longitude` DOUBLE NULL DEFAULT 0 AFTER `latitude`,
ADD COLUMN `font_size` INT(3) NULL DEFAULT 0 AFTER `longitude`,
ADD COLUMN `font_style` VARCHAR(30) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NULL AFTER `font_size`,
ADD COLUMN `color` VARCHAR(10) NULL AFTER `font_style`,
ADD COLUMN `href` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NULL AFTER `color`,
ADD COLUMN `href_title` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NULL AFTER `href`,
ADD COLUMN `is_deleted` INT(1) NULL DEFAULT 0 AFTER `href_title`;

ALTER TABLE `omart_db`.`omart_poi_picture` 
CHANGE COLUMN `created_at` `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ;

ALTER TABLE `omart_db`.`omart_poi_picture` 
CHANGE COLUMN `image_url` `image_url` VARCHAR(5000) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NULL DEFAULT NULL ;
