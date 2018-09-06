CREATE TABLE `omart_category` (
	`id` INT ( 11 ) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`parent_id` int(11) DEFAULT NULL,
	`name` VARCHAR ( 255 ) COLLATE utf8_unicode_ci NOT NULL,
	`image` text COLLATE utf8_unicode_ci NOT NULL,
  `title_color` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `background_color` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8_unicode_ci,
	`order` INT ( 11 ) default 1000
) ENGINE = INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COLLATE = utf8_unicode_ci;

CREATE TABLE `omart_point_of_interest` (
	`cat_id` INT ( 11 ) NOT NULL COMMENT 'REFERENCES KEY to omart_category',
	`id` INT ( 11 ) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`name` VARCHAR ( 255 ) COLLATE utf8_unicode_ci NOT NULL,
	`description` text COLLATE utf8_unicode_ci DEFAULT NULL,
	`address` text COLLATE utf8_unicode_ci NOT NULL,
	`ward` VARCHAR ( 255 ) COLLATE utf8_unicode_ci NOT NULL,
	`district` VARCHAR ( 255 ) COLLATE utf8_unicode_ci NOT NULL,
	`province` VARCHAR ( 255 ) COLLATE utf8_unicode_ci NOT NULL,
	`phone` text COLLATE utf8_unicode_ci NOT NULL COMMENT 'list phone split by comma',
	`latitude` DOUBLE NOT NULL,
	`longitude` DOUBLE NOT NULL,
	`location` POINT,
	`open_hour` VARCHAR ( 255 ) COLLATE utf8_unicode_ci NOT NULL,
	`close_hour` VARCHAR ( 255 ) COLLATE utf8_unicode_ci NOT NULL,
	`opening_state` TINYINT ( 1 ) NOT NULL DEFAULT 1 COMMENT 'user can update for off day DEFAULT 1 is True',
	`avatar_image` JSON DEFAULT NULL COMMENT 'Array image ',
	`cover_image` JSON DEFAULT NULL COMMENT 'Array image ',
	`featured_image` JSON DEFAULT NULL COMMENT 'Array image ',
	`created_by` VARCHAR ( 255 ) COLLATE utf8_unicode_ci DEFAULT 'system',
  `created_at` TIMESTAMP NOT NULL,
	`updated_by` VARCHAR ( 255 ) COLLATE utf8_unicode_ci DEFAULT NULL,
  `updated_at` TIMESTAMP NOT NULL DEFAULT NOW() ON UPDATE NOW()
) ENGINE = INNODB AUTO_INCREMENT = 3 DEFAULT CHARSET = utf8 COLLATE = utf8_unicode_ci;

CREATE TABLE `omart_poi_item` (
	`poi_id` INT ( 11 ) NOT NULL COMMENT 'REFERENCES KEY to omart_point_of_interest',
	`id` INT ( 11 ) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`name` VARCHAR ( 255 ) COLLATE utf8_unicode_ci NOT NULL,
	`description` text COLLATE utf8_unicode_ci DEFAULT NULL,
	`unit_price` DOUBLE NOT NULL,
	`type` VARCHAR ( 255 ) COLLATE utf8_unicode_ci COMMENT 'like tag',
	`avatar_image` JSON DEFAULT NULL COMMENT 'Array image ',
	`cover_image` JSON DEFAULT NULL COMMENT 'Array image ',
	`featured_image` JSON DEFAULT NULL COMMENT 'Array image ',
	`created_by` VARCHAR ( 255 ) COLLATE utf8_unicode_ci DEFAULT 'system',
  `created_at` TIMESTAMP NOT NULL,
	`updated_by` VARCHAR ( 255 ) COLLATE utf8_unicode_ci DEFAULT NULL,
  `updated_at` TIMESTAMP NOT NULL DEFAULT NOW() ON UPDATE NOW()
) ENGINE = INNODB AUTO_INCREMENT = 7 DEFAULT CHARSET = utf8 COLLATE = utf8_unicode_ci;