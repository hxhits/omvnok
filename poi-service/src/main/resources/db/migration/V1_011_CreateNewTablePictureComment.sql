CREATE TABLE `omart_poi_picture_comment` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
`picture_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_poi_picture',
`comment` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
`created_at` timestamp NULL DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `omart_poi_picture` ADD COLUMN comment_count INT DEFAULT 0 AFTER like_count;

ALTER TABLE `omart_db`.`omart_poi_picture_comment` 
CHANGE COLUMN `comment` `comment` VARCHAR(255) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL DEFAULT NULL ;

ALTER TABLE `omart_db`.`omart_poi_comment` 
CHANGE COLUMN `comment` `comment` VARCHAR(255) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL DEFAULT NULL ;

ALTER TABLE `omart_db`.`omart_poi_picture` 
CHANGE COLUMN `title` `title` VARCHAR(255) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL DEFAULT NULL ;

ALTER TABLE `omart_db`.`omart_point_of_interest` 
CHANGE COLUMN `description` `description` TEXT CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL DEFAULT NULL ;

