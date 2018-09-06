CREATE TABLE `omart_contact` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `contact_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `omart_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `phone_number` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `email` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `type` int(1) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci




ALTER TABLE `omart_db`.`omart_message_channel` 
CHANGE COLUMN `poi_id` `poi_id` INT(11) NOT NULL DEFAULT '0' ;


--------------------
CREATE TABLE `omart_call_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` int(1) DEFAULT '0',
  `call_sid` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `parent_call_sid` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `sender_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `recipient_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `created_at` timestamp NULL DEFAULT NULL,
  `started_at` timestamp NULL DEFAULT NULL,
  `completed_at` timestamp NULL DEFAULT NULL,
  `call_duration` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `call_sid_UNIQUE` (`call_sid`),
  UNIQUE KEY `parent_call_sid_UNIQUE` (`parent_call_sid`)
) ENGINE=InnoDB AUTO_INCREMENT=21936 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci