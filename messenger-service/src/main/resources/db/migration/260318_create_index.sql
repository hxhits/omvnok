ALTER TABLE `omart_db`.`omart_message_history` ADD INDEX(channel_sid);
 
ALTER TABLE `omart_db`.`omart_message_history`
    ADD COLUMN  `raw_content` varchar(3000) COLLATE utf8_unicode_ci DEFAULT '';