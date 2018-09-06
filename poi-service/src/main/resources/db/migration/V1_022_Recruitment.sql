CREATE TABLE `omart_recruitment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `position` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_category',
  `salary` varchar(50) COLLATE utf8_unicode_ci DEFAULT '',
  `work_location` varchar(100) COLLATE utf8_unicode_ci DEFAULT '',
  `education_level` varchar(50) COLLATE utf8_unicode_ci DEFAULT '',
  `experience_level` varchar(50) COLLATE utf8_unicode_ci DEFAULT '',
  `quantity` int(6) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `expired_at` timestamp NULL DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `requirement` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `benefit` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `contact_info` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `view_count` int(6) DEFAULT '0',
  `position_type` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `state` int(2) NOT NULL DEFAULT '0' COMMENT '0: DANG TUYEN, 1: HET HAN',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci

ALTER TABLE `omart_db`.`omart_recruitment`
    ADD COLUMN `poi_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_point_of_interest';