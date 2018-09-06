ALTER TABLE `omart_db`.`omart_poi_notification` 
CHANGE COLUMN `poi_id` `poi_id` INT(11) NULL COMMENT 'REFERENCES KEY to omart_point_of_interest' ;

    ALTER TABLE `omart_db`.`omart_category`
    ADD COLUMN  `unit_price` int(11) DEFAULT 0;

 ALTER TABLE `omart_db`.`omart_poi_notification`
    ADD COLUMN  `bookcar_id` int(11) DEFAULT NULL COMMENT 'REFERENCES KEY to omart_book_car';
    
        ALTER TABLE `omart_db`.`omart_poi_notification`
    ADD COLUMN  `province` int(11) DEFAULT NULL;
    
    ALTER TABLE `omart_db`.`omart_poi_notification`
    ADD COLUMN  `district` int(11) DEFAULT NULL;
    
     ALTER TABLE `omart_db`.`omart_poi_notification`
 ADD COLUMN  `is_active` bit(1) NOT NULL DEFAULT 1 COMMENT '1 is active, 0 is un-active';
    
geodistance_poi_notification_v1
geodistance_poi_notification_v2