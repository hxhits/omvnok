
ALTER TABLE `omart_db`.`omart_timeline`
ADD COLUMN  `is_report_abuse` bit(1) default 0;

CREATE TABLE `omart_timeline_report_abuse` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timeline_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_timeline',
  `user_from` int(11) NOT NULL COMMENT 'reference to omart_user_profile',
  `user_to` int(11) NOT NULL COMMENT 'reference to omart_user_profile',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `report_type` int(11) NOT NULL DEFAULT '0' COMMENT '0 = abuse, 1 = hide',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

===== 
geodistance_timeline_v5
geodistance_my_timeline_v3
geodistance_timeline_v4
=====

CREATE TABLE `omart_db`.`omart_timeline_lock` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_from` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `user_to` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;