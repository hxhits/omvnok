ALTER TABLE `omart_db`.`omart_order`
ADD COLUMN  `seller_state_reason`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL;

ALTER TABLE `omart_db`.`omart_order`
ADD COLUMN  `bookcar_id` int(11) COMMENT 'REFERENCES KEY to omart_book_car';

ALTER TABLE `omart_db`.`omart_order`
ADD COLUMN  `driver_id` varchar(255) COLLATE utf8_unicode_ci COMMENT 'REFERENCES KEY to omart_driver_info';

CREATE TABLE `omart_home_banner` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `poi_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_point_of_interest',
  `image` text COLLATE utf8_unicode_ci,
  `banner_type` int(2) DEFAULT '0' COMMENT '0 mobile, 1 desktop',
  `created_by` varchar(255) COLLATE utf8_unicode_ci DEFAULT 'system',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_by` varchar(255) COLLATE utf8_unicode_ci DEFAULT 'system',
  `updated_at` timestamp NULL DEFAULT NULL,
  `is_approved` bit(1) NOT NULL DEFAULT b'0' COMMENT '1 = true, 0 = false',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `omart_home_feature` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `image` text COLLATE utf8_unicode_ci,
  `title_color` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `desc_color` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `feature_id` int(1) DEFAULT '0' COMMENT '1= driver, 2= recruit, 3 = shop ',
  `feature_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `feature_name_en` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` varchar(255) COLLATE utf8_unicode_ci DEFAULT 'system',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_by` varchar(255) COLLATE utf8_unicode_ci DEFAULT 'system',
  `updated_at` timestamp NULL DEFAULT NULL,
  `is_approved` bit(1) NOT NULL DEFAULT b'0' COMMENT '1 = true, 0 = false',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE `omart_poi_deliverer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `poi_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_point_of_interest',
  `driver_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT 'REFERENCES KEY to omart_driver_info',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `omart_report_abuse` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `poi_id` int(11) DEFAULT NULL COMMENT 'REFERENCES KEY to omart_point_of_interest',
  `user_profile_id` int(11) NOT NULL COMMENT 'reference to profile id',
  `owner_profile_id` int(11) NOT NULL,
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `side` int(1) NOT NULL DEFAULT '0' COMMENT '0 = user->poi, 1 = poi->user',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `push_notification_token_new` (
  `user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `token` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `client` int(2) NOT NULL DEFAULT '0' COMMENT '1 = android,2 = ios, 3 = web',
  PRIMARY KEY (`user_id`,`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;