ALTER TABLE `omart_db`.`omart_video_room`
CHANGE token sender_token varchar(3000) COLLATE utf8_unicode_ci DEFAULT '';


ALTER TABLE `omart_db`.`omart_video_room`
    ADD COLUMN `recipient_token`  varchar(3000) COLLATE utf8_unicode_ci DEFAULT '';

ALTER TABLE `omart_db`.`omart_video_room`
    ADD COLUMN `room_sid`  varchar(3000) COLLATE utf8_unicode_ci DEFAULT '';

ALTER TABLE `omart_db`.`omart_video_room`
    ADD COLUMN `poi_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_point_of_interest';



DROP TABLE IF EXISTS `omart_message_channel`;
CREATE TABLE `omart_message_channel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `channel_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `channel_sid` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `sender_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `recipient_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `poi_id` int(11) DEFAULT '0',
  `sender_deleted` int(1) DEFAULT '0',
  `recipient_deleted` int(1) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT NULL,
  `sender_last_index` int(11) DEFAULT '0',
  `recipient_last_index` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `channel_id_UNIQUE` (`channel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci


ALTER TABLE `omart_db`.`push_notification_token`
    ADD COLUMN `client` int(2) DEFAULT '0' COMMENT '1 = android,2 = ios, 3 = web';

    ALTER TABLE `omart_db`.`omart_message_channel`
ADD UNIQUE INDEX `channel_id_UNIQUE` (`channel_id` ASC);

ALTER TABLE `omart_db`.`omart_message_channel`
ADD UNIQUE INDEX `channel_sid_UNIQUE` (`channel_sid` ASC);


ALTER TABLE `omart_db`.`omart_message_channel`
    ADD COLUMN `sender_last_index` int(11) DEFAULT '0';
ALTER TABLE `omart_db`.`omart_message_channel`
    ADD COLUMN `recipient_last_index` int(11) DEFAULT '0';


CREATE TABLE `omart_message_history` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `channel_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
      `channel_sid` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
      `sender_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
      `recipient_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
      `message_index` int(11) DEFAULT '0',
      `content` VARCHAR(255) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL DEFAULT '',
      `created_at` timestamp NULL DEFAULT NULL,
      PRIMARY KEY (`id`),
      UNIQUE KEY `channel_id_UNIQUE` (`channel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci

----------------------21/3/2018----------------------
 ALTER TABLE `omart_db`.`omart_call_status`
    ADD COLUMN  `call_type` int(2) DEFAULT '0' COMMENT '1 VOICE CALL, 2 VIDEO CALL,..';
    
     ALTER TABLE `omart_db`.`omart_call_status`
    ADD COLUMN  `room_id` int(11) DEFAULT NULL;
