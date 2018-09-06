ALTER TABLE `omart_db`.`omart_recruitment_position`
ADD COLUMN  `image` varchar(550) COLLATE utf8_unicode_ci DEFAULT NULL;

CREATE TABLE `omart_db`.`omart_poi_follow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_profile_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_user_profile',
  `poi_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_point_of_interest',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;



ALTER TABLE `omart_db`.`omart_poi_item`
ADD COLUMN  `posted_at` timestamp null default null COMMENT 'the date post item to omart product';

ALTER TABLE `omart_db`.`omart_poi_item`
ADD COLUMN  `cat_id` int(11) COMMENT 'REFERENCES KEY to omart_category';

ALTER TABLE `omart_db`.`omart_poi_item`
ADD COLUMN  `is_posted` bit(1) default 0 COMMENT '0 = is not posted to omart product, 1 = posted to omart product';

ALTER TABLE `omart_db`.`omart_point_of_interest`
ADD COLUMN  `discount` int(3) default 0;

ALTER TABLE `omart_db`.`omart_order`
ADD COLUMN  `is_calc_discount` bit(1) default 0 COMMENT '0 = is not calculated discount, 1 = is calculated discount';

ALTER TABLE `omart_db`.`omart_user_profile`
ADD COLUMN  `coin` int(8) default 0;

=====================

CREATE TABLE `omart_coin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_profile_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_user_profile',
  `user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `mart_coin` int(11) DEFAULT '0' COMMENT '100 coin = 1 martcoin',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_profile_id_UNIQUE` (`user_profile_id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `omart_db`.`omart_order`
ADD COLUMN  `coin_extra` int(8) default 0;

===procedure== > geodistance_news_enhance_v1