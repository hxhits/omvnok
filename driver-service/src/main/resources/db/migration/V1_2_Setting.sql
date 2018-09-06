ALTER TABLE `omart_db`.`omart_driver_push_token` 
ADD COLUMN  `ring_index` int(2) NOT NULL DEFAULT 0;

ALTER TABLE `omart_db`.`omart_driver_push_token` 
ADD COLUMN  `is_disabled` bit(1) NOT NULL DEFAULT 0 COMMENT '0 = enabled, 1 = disabled';

CREATE TABLE `omart_driver_setting` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ring_index` int(2) NOT NULL DEFAULT '0',
  `is_disabled` bit(1) NOT NULL DEFAULT b'0' COMMENT '0 = enabled, 1 = disabled',
  `user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci
 