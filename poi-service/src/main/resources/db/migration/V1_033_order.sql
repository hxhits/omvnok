CREATE TABLE `omart_db`.`omart_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `user_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `user_avatar` varchar(550) COLLATE utf8_unicode_ci DEFAULT NULL,
  `user_phone` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `user_latitude` double NOT NULL default 0.0,
  `user_longitude` double NOT NULL default 0.0,
  `poi_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_point_of_interest',
  `shipping_address` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `quantity` int(3) NOT NULL default 0,
  `ship_fee` int(11) NOT NULL default 0,
  `sub_total` int(11) NOT NULL default 0,
  `total` int(11) NOT NULL default 0,
  `payment_type` int(2) NOT NULL default 0 COMMENT 'cash = 0 ',
  `state` int(2) NOT NULL default 0 COMMENT 'waiting = 0,approve = 1,cancel = 2,reject = 3',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `omart_db`.`omart_order_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_order',
  `item_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_poi_item',
  `item_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `item_avatar` varchar(550) COLLATE utf8_unicode_ci DEFAULT NULL,
  `unit_price` double NOT NULL,
  `quantity` int(3) NOT NULL default 0,
  `total_price` int(3) NOT NULL default 0,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


ALTER TABLE `omart_db`.`omart_point_of_interest` 
ADD COLUMN   `is_approved`  BIT(1) DEFAULT 1 COMMENT 'be reviewd by omart admin, 0 = is not approved yet, 1 is approved';

ALTER TABLE `omart_db`.`omart_point_of_interest` 
ADD COLUMN   `reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT 'the reason why omart admin is not approved';

ALTER TABLE `omart_db`.`omart_point_of_interest` 
CHANGE COLUMN `is_approved` `is_approved` BIT(1) NULL DEFAULT 0 COMMENT 'be reviewd by omart admin, 0 = is not approved yet, 1 is approved' ;


geodistance_news_enhance_v1
geodistance_poi_enhance_v1
geodistance_poi_enhance_v2
geodistance_poi_enhance_v4
geodistance_poi_notification_v2
geodistance_poi_notification_v4
geodistance_poi_notification_v4

ALTER TABLE `omart_db`.`omart_order`
ADD COLUMN  `profile_id` int(11) NOT NULL comment 'reference to profile id';


ALTER TABLE `omart_db`.`omart_order`
ADD COLUMN  `purchase_order_id` varchar(255) NOT NULL comment 'purchase order id auto generate';

ALTER TABLE `omart_db`.`omart_order`
ADD COLUMN  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE `omart_db`.`omart_order_detail`
ADD COLUMN  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE `omart_db`.`omart_order`
ADD COLUMN  `note` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

ALTER TABLE `omart_db`.`omart_order`
ADD COLUMN  `buyer_state` int(2) Not null default 0 comment '0 = accept order , 1 = cancelled ordrer';

ALTER TABLE `omart_db`.`omart_order`
ADD COLUMN  `seller_state` int(2) Not null default 0 comment '0 = waiting, 1 = invalided, 2 = cancelled, 3 = processing, 4 = delivery, 5 = completed';

ALTER TABLE `omart_db`.`omart_order`
ADD COLUMN `is_deleted` bit(1) DEFAULT 0 comment ' 0 = not deleted yet, 1 = deleted';
==================================================================================================================================================================================
CREATE TABLE `omart_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `profile_id` int(11) NOT NULL COMMENT 'reference to profile id',
  `user_latitude` double NOT NULL DEFAULT '0',
  `user_longitude` double NOT NULL DEFAULT '0',
  `poi_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_point_of_interest',
  `shipping_address` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `quantity` int(3) NOT NULL DEFAULT '0',
  `ship_fee` int(11) NOT NULL DEFAULT '0',
  `sub_total` int(11) NOT NULL DEFAULT '0',
  `total` decimal(20,0) NOT NULL DEFAULT '0',
  `payment_type` int(2) NOT NULL DEFAULT '0' COMMENT 'cash = 0 ',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `purchase_order_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT 'purchase order id auto generate',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `note` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `buyer_state` int(2) NOT NULL DEFAULT '0' COMMENT '0 = accept order , 1 = cancelled ordrer',
  `seller_state` int(2) NOT NULL DEFAULT '0' COMMENT '0 = waiting, 1 = invalided, 2 = cancelled, 3 = processing, 4 = delivery, 5 = completed',
  `is_deleted` bit(1) DEFAULT b'0' COMMENT ' 0 = not deleted yet, 1 = deleted',
  `delivery_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE `omart_order_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_order',
  `item_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_poi_item',
  `item_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `item_avatar` varchar(550) COLLATE utf8_unicode_ci DEFAULT NULL,
  `unit_price` double NOT NULL,
  `quantity` int(3) NOT NULL DEFAULT '0',
  `total_price` decimal(20,0) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

