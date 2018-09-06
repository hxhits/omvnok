DROP TABLE IF EXISTS `omart_poi_action`;

CREATE TABLE `omart_poi_action` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
`poi_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_point_of_interest',
`action_type` int(2) COMMENT '1 LIKE, 2 DISLIKE, 3 FAVORITE',
`created_at` timestamp NULL DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `omart_poi_comment`;

CREATE TABLE `omart_poi_comment` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
`poi_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_point_of_interest',
`comment` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
`created_at` timestamp NULL DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `omart_poi_picture`;

CREATE TABLE `omart_poi_picture` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
`poi_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_point_of_interest',
`image_url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
`comment` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
`created_at` timestamp NULL DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `omart_db`.`omart_point_of_interest`
	ADD COLUMN `view_count` int(6) NULL DEFAULT 0,
    ADD COLUMN `rate` float(3) NULL DEFAULT 0;