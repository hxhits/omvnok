ALTER TABLE `omart_db`.`omart_point_of_interest`
ADD COLUMN `is_deleted` bit(1) NOT NULL DEFAULT 0 COMMENT '1 = deleted, 0 = existing';

ALTER TABLE `omart_db`.`omart_point_of_interest`
ADD COLUMN `display_state` int(3) DEFAULT 0;

ALTER TABLE `omart_db`.`omart_poi_notification`
ADD COLUMN `recruit_id` int(11) default NULL COMMENT 'REFERENCES KEY to omart_recruitment';

ALTER TABLE `omart_db`.`omart_timeline`
ADD COLUMN `is_moment` bit(1) NOT NULL DEFAULT 0 COMMENT '0 = false , 1 = true';

ALTER TABLE `omart_db`.`omart_poi_notification`
ADD COLUMN `is_deleted` bit(1) NOT NULL DEFAULT 0 COMMENT '1 = deleted, 0 = existing';

ALTER TABLE `omart_db`.`omart_poi_notification`
ADD COLUMN  `updated_at` timestamp NULL DEFAULT NULL;

ALTER TABLE `omart_db`.`omart_poi_notification`
ADD COLUMN  `font_size` int(3) DEFAULT '0';

ALTER TABLE `omart_db`.`omart_poi_notification`
ADD COLUMN   `font_style` varchar(30) COLLATE utf8_unicode_ci DEFAULT '';

ALTER TABLE `omart_db`.`omart_poi_notification`
ADD COLUMN  `color` varchar(10) COLLATE utf8_unicode_ci DEFAULT '';

ALTER TABLE `omart_db`.`omart_point_of_interest`
ADD COLUMN  `email` varchar(320) COLLATE utf8_unicode_ci DEFAULT '';

ALTER TABLE `omart_db`.`omart_point_of_interest`
ADD COLUMN  `web_address` varchar(255) COLLATE utf8_unicode_ci DEFAULT '';

ALTER TABLE `omart_db`.`omart_recruitment`
    ADD COLUMN `title` varchar(255) COLLATE utf8_unicode_ci DEFAULT '';
    

CREATE TABLE `omart_district_follow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `province` int(11) DEFAULT '0',
  `district` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci
