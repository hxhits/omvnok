CREATE TABLE `omart_message_channel` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
`channel_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
`sender_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
`recipient_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
`poi_id` int(11) DEFAULT 0,
`sender_deleted` int(1) DEFAULT 0,
`recipient_deleted` int(1) DEFAULT 0,
`created_at` timestamp NULL DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
