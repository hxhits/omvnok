DROP TABLE IF EXISTS `omart_poi_picture_action`;

CREATE TABLE `omart_poi_picture_action` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
`picture_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_poi_picture',
`action_type` int(2) COMMENT '1 LIKE, 2 DISLIKE',
`created_at` timestamp NULL DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `omart_poi_picture` ADD COLUMN like_count INT DEFAULT 0 AFTER title;
ALTER TABLE `omart_point_of_interest` ADD COLUMN share_count INT DEFAULT 0 AFTER view_count;