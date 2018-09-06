CREATE TABLE `messenger_delete` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `friend_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `shop_id` INT(11) NULL,
  `deleted_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


ALTER TABLE `omart_db`.`messenger_history`
    ADD COLUMN `sender_deleted` BIT(1) NULL DEFAULT 0,
    ADD COLUMN `recipient_deleted` BIT(1) NULL DEFAULT 0;
