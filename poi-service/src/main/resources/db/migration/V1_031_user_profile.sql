CREATE TABLE `omart_user_profile` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `cover` varchar(550) COLLATE utf8_unicode_ci DEFAULT NULL,
  `avatar` varchar(550) COLLATE utf8_unicode_ci DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `lives_in` int(11) DEFAULT NULL COMMENT 'Reference to province Table',
  `phone` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `sex` int(2) NOT NULL COMMENT '0 = undefine, 1 = female, 2 = male',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22817 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci