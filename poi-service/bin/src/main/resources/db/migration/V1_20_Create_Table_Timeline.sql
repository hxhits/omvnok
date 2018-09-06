CREATE TABLE `omart_timeline` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `images` json DEFAULT NULL COMMENT 'Array image ',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `timeline_type` int(1) DEFAULT '0' COMMENT '0 is omart system other of user',
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `omart_timeline_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `timeline_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_timeline',
  `comment` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `omart_timeline_action` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `timeline_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_timeline',
  `action_type` int(2) DEFAULT NULL COMMENT '1 LIKE, 2 DISLIKE',
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARSET=utf8 COLLATE=utf8_unicode_ci;


	ALTER TABLE `omart_db`.`omart_timeline`
    ADD COLUMN `like_count` int(6) DEFAULT '0';
    
    ALTER TABLE `omart_db`.`omart_timeline`
    ADD COLUMN `comment_count` int(6) DEFAULT '0';
    
    ALTER TABLE `omart_db`.`omart_timeline`
    ADD COLUMN `font_size` int(3) DEFAULT '0';
    
    ALTER TABLE `omart_db`.`omart_timeline`
    ADD COLUMN `font_style` varchar(30) DEFAULT '';
    
	ALTER TABLE `omart_db`.`omart_timeline`
    ADD COLUMN `color` varchar(10) DEFAULT '';
    
    ALTER TABLE `omart_db`.`omart_timeline`
    ADD COLUMN `href` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT '';
    
    ALTER TABLE `omart_db`.`omart_timeline`
    ADD COLUMN `href_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT '';
    
================================= omart_timeline_action  =================================
        
DELIMITER //

CREATE TRIGGER count_like_omart_timeline_action_after_update
AFTER UPDATE ON omart_timeline_action FOR EACH ROW
BEGIN

IF(NEW.action_type = 1) THEN
	 UPDATE omart_timeline SET omart_timeline.like_count = omart_timeline.like_count + 1       
	 WHERE omart_timeline.id = NEW.timeline_id; 
ELSE IF(NEW.action_type = 2) THEN
	 UPDATE omart_timeline SET omart_timeline.like_count = omart_timeline.like_count - 1       
	 WHERE omart_timeline.id = NEW.timeline_id;
END IF;
END IF;
END; //

DELIMITER ;

================================= omart_timeline_action  =================================


DELIMITER //

CREATE TRIGGER count_like_omart_timeline_action_after_insert
AFTER INSERT
   ON omart_timeline_action FOR EACH ROW
BEGIN
  UPDATE omart_timeline SET omart_timeline.like_count = omart_timeline.like_count + 1       
  WHERE omart_timeline.id = NEW.timeline_id; 
END; //

DELIMITER ;

================================= omart_timeline_comment when post a comment  =================================

DELIMITER //

CREATE TRIGGER count_comment_omart_timeline_comment_after_insert
AFTER INSERT
   ON omart_timeline_comment FOR EACH ROW
BEGIN
  UPDATE omart_timeline SET omart_timeline.comment_count = omart_timeline.comment_count + 1       
  WHERE omart_timeline.id = NEW.timeline_id; 
END; //

DELIMITER ;

====================================add new column is_deleted===================
	ALTER TABLE `omart_db`.`omart_timeline`
    ADD COLUMN `is_deleted` bit(1) NOT NULL DEFAULT 0 COMMENT '0 = NO DELETING, 1 = DELETED';
    
        ALTER TABLE `omart_db`.`omart_timeline`
    ADD COLUMN `custom_user_avatar` varchar(255) DEFAULT '';
    
     ALTER TABLE `omart_db`.`omart_timeline`
    ADD COLUMN `custom_user_name` varchar(100) DEFAULT '';