CREATE TABLE `omart_news` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8_unicode_ci,
  `thumbnail_url` varchar(550),
  `banner_url` varchar(550),
  is_read BIT(1) NULL DEFAULT 0,
  `news_type` TINYINT(4) NULL DEFAULT 0 ,
  `created_by` varchar(255) COLLATE utf8_unicode_ci DEFAULT 'system',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


ALTER TABLE `omart_db`.`omart_poi_own`
ADD COLUMN `is_paid` BIT(1) NULL DEFAULT 0,
ADD COLUMN `trial_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

ALTER TABLE `omart_db`.`messenger_history`
ADD COLUMN `shop_id` INT(11) DEFAULT 0;
