ALTER TABLE `omart_db`.`omart_news`
	ADD COLUMN `longitude` double DEFAULT '0',
    ADD COLUMN `latitude` double DEFAULT '0',
    ADD COLUMN `poi_id` int(11) COMMENT 'REFERENCES KEY to omart_point_of_interest',
    ADD COLUMN `approval` tinyint(1) DEFAULT '0' COMMENT '0: not approval 1: approved';