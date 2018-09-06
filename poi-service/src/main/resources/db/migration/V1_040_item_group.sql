ALTER TABLE `omart_db`.`omart_report_abuse`
ADD COLUMN  `order_id` int(11) DEFAULT NULL COMMENT 'REFERENCES KEY to omart_order';

ALTER TABLE `omart_db`.`omart_point_of_interest`
ADD COLUMN  `delivery_radius` int(11) default 0;

ALTER TABLE `omart_db`.`omart_point_of_interest`
ADD COLUMN  `currency` int(2) default 0 COMMENT '0: VND, 1: USD';

ALTER TABLE `omart_db`.`omart_report_abuse`
ADD COLUMN  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE `omart_db`.`omart_report_abuse`
ADD COLUMN  `driver_id` varchar(255) COLLATE utf8_unicode_ci COMMENT 'REFERENCES KEY to omart_driver_info';

ALTER TABLE `omart_db`.`omart_report_abuse` 
CHANGE COLUMN `poi_id` `poi_id` INT(11) NOT NULL COMMENT 'REFERENCES KEY to omart_point_of_interest' ,
CHANGE COLUMN `user_profile_id` `user_profile_id` INT(11) NULL COMMENT 'reference to profile id' ;

ALTER TABLE `omart_db`.`omart_report_abuse` 
CHANGE COLUMN `side` `side` INT(1) NOT NULL DEFAULT '0' COMMENT '0 = user->poi, 1 = poi->user, 2 = poi->shipper' ;

CREATE TABLE `omart_poi_item_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `poi_id` int(11) DEFAULT NULL COMMENT 'REFERENCES KEY to omart_point_of_interest',
  `avatar` varchar(550) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE `omart_item_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_poi_item',
  `group_id` int(11) DEFAULT NULL COMMENT 'REFERENCES KEY to omart_poi_item_group',
  `poi_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_point_of_interest',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;