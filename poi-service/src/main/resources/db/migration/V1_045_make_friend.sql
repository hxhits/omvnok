CREATE TABLE `omart_db`.`omart_user_friend_request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_user_profile',
  `recipient` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_user_profile',	
  `state` int(2) DEFAULT '0' comment '0 = wait, 1 = accept, 2 = reject',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
 

 
 
CREATE TABLE `omart_db`.`omart_user_friend` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_user_profile',
  `friend_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_user_profile',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
 
 
 =============8/31/2018=======
 ALTER TABLE `omart_db`.`omart_poi_action` 
CHANGE COLUMN `user_id` `user_id` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ;
 
 
 geodistance_poi_notification_v2