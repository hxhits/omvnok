ALTER TABLE `omart_db`.`omart_driver_info` 
ADD COLUMN  `reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT 'the reason why omart admin block driver';


ALTER TABLE `omart_db`.`omart_driver_car_type`
ADD COLUMN  `unit_price_2km` int(11) DEFAULT '0';