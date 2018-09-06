ALTER TABLE `omart_db`.`omart_point_of_interest`
    ADD COLUMN `last_notification` timestamp NULL DEFAULT NULL;
    
ALTER TABLE `omart_db`.`omart_timeline`
    ADD COLUMN `is_private` bit(1) NOT NULL DEFAULT 0 COMMENT '1 = private, 0 = public';