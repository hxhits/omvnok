ALTER TABLE `omart_db`.`omart_order`
ADD COLUMN  `delivery_period` int(2) not null default 0;

==== store procedure ====
filter_poi_by_category

======recruit===========
ALTER TABLE `omart_db`.`omart_recruitment_position_level`
ADD COLUMN  `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL;


ALTER TABLE `omart_db`.`omart_order`
ADD COLUMN  `user_current_address` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL;

ALTER TABLE `omart_db`.`omart_order`
ADD COLUMN  `receiver_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL;

ALTER TABLE `omart_db`.`omart_order`
ADD COLUMN  `receiver_phone` varchar(255) COLLATE utf8_unicode_ci NOT NULL;
