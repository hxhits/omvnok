CREATE TABLE `omart_category_follow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cat_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_category',
  `user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE `omart_poi_notification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
   `poi_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_point_of_interest',
  `cat_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_category',
   `user_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `user_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `user_avatar` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `images` json DEFAULT NULL COMMENT 'Array image ',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `notification_type` int(1) DEFAULT 0 COMMENT '0 is omart system other of user',
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


    ALTER TABLE `omart_db`.`omart_poi_notification`
    ADD COLUMN  `avatar_image` json DEFAULT NULL COMMENT 'Array image';

    ALTER TABLE `omart_db`.`omart_poi_notification`
    ADD COLUMN  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL;

    ALTER TABLE `omart_db`.`omart_poi_notification`
    ADD COLUMN   `address` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL;

    ALTER TABLE `omart_db`.`omart_poi_notification`
    ADD COLUMN  `ward` int(11) NOT NULL;

    ALTER TABLE `omart_db`.`omart_poi_notification`
    ADD COLUMN  `district` int(11) NOT NULL;

    ALTER TABLE `omart_db`.`omart_poi_notification`
    ADD COLUMN  `province` int(11) NOT NULL;

    ALTER TABLE `omart_db`.`omart_poi_notification`
    ADD COLUMN   `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'list phone split by comma';

     ALTER TABLE `omart_db`.`omart_timeline`
     ADD COLUMN `updated_at` timestamp NULL DEFAULT NULL;
