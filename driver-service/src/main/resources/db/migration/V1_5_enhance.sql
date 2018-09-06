
ALTER TABLE `omart_db`.`omart_book_car`
ADD COLUMN  `service_type` int(1) NOT NULL DEFAULT 0 COMMENT '0 = transport, 1 = delivery';

ALTER TABLE `omart_db`.`omart_book_car`
ADD COLUMN  `receiver_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL;

ALTER TABLE `omart_db`.`omart_book_car`
ADD COLUMN  `receiver_phone` varchar(255) COLLATE utf8_unicode_ci NOT NULL;

ALTER TABLE `omart_db`.`omart_book_car`
ADD COLUMN  `order_id` int(11);

ALTER TABLE `omart_db`.`omart_book_car` 
CHANGE COLUMN `receiver_name` `receiver_name` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NULL ,
CHANGE COLUMN `receiver_phone` `receiver_phone` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NULL ;
