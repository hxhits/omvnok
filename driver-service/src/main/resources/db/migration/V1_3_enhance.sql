ALTER TABLE `omart_db`.`omart_driver_info` 
ADD COLUMN  `is_blocked` bit(1) NOT NULL DEFAULT 0;

ALTER TABLE `omart_db`.`omart_driver_push_token` 
ADD COLUMN  `is_blocked` bit(1) NOT NULL DEFAULT 0;

ALTER TABLE `omart_db`.`omart_driver_info` 
ADD COLUMN  `car_image` json DEFAULT null;

ALTER TABLE `omart_db`.`omart_driver_info` 
ADD COLUMN  `driver_licence_image` json DEFAULT NULL;

ALTER TABLE `omart_db`.`omart_driver_info` 
ADD COLUMN  `identity_card_image` json DEFAULT NULL;

ALTER TABLE `omart_db`.`omart_driver_info` 
ADD COLUMN `rate_number` float(5) NOT NULL DEFAULT 0;

ALTER TABLE `omart_db`.`omart_driver_info` 
ADD COLUMN `total_star` int(11) NOT NULL DEFAULT 0;

ALTER TABLE `omart_db`.`omart_driver_info` 
ADD COLUMN  `contract_id` varchar(255) NOT NULL default '';

ALTER TABLE `omart_db`.`omart_driver_info` 
ADD COLUMN  `account_type` int(2) NOT NULL DEFAULT 1;

ALTER TABLE `omart_db`.`omart_driver_info` 
ADD UNIQUE INDEX `contract_id_UNIQUE` (`contract_id` ASC);
