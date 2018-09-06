ALTER TABLE `omart_db`.`omart_order` 
CHANGE COLUMN `sub_total` `sub_total` DOUBLE NOT NULL DEFAULT '0' ;

ALTER TABLE `omart_db`.`omart_order` 
CHANGE COLUMN `total` `total` DOUBLE NOT NULL DEFAULT '0' ;

ALTER TABLE `omart_db`.`omart_order_detail` 
CHANGE COLUMN `total_price` `total_price` DOUBLE NOT NULL DEFAULT '0' ;


CREATE TABLE `omart_user_bod` (
  `id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `bod_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `user_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `password` text COLLATE utf8_unicode_ci,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_UNIQUE` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE `omart_poi_bod` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_bod` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT 'REFERENCES KEY to omart_user_bod',
  `poi_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_point_of_interest',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `poi_id_UNIQUE` (`poi_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci