ALTER TABLE `omart_db`.`omart_timeline`
ADD COLUMN  `place_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL;

ALTER TABLE `omart_db`.`omart_timeline`
ADD COLUMN  `is_save_history` bit(1) NOT NULL DEFAULT 0 COMMENT '1 = save, 0 = not save';

===== 
geodistance_my_timeline_v2
geodistance_my_timeline_v3
geodistance_timeline_v4
geodistance_timeline_v1
geodistance_timeline_v2
=====

ALTER TABLE `omart_db`.`omart_timeline`
ADD COLUMN  `poi_id` int(11) default NULL COMMENT 'REFERENCES KEY to omart_point_of_interest';

ALTER TABLE `omart_db`.`omart_timeline` 
CHANGE COLUMN `timeline_type` `timeline_type` INT(1) NULL DEFAULT '0' COMMENT '0 is omart system other of user, 3 = shop post.' ;
