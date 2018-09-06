ALTER TABLE `omart_db`.`omart_poi_notification` 
ADD COLUMN `pos_id` INT(11) NULL AFTER `cat_id`;

ALTER TABLE `omart_db`.`omart_recruitment` 
ADD COLUMN `position_level_id` INT(11) NULL AFTER `position_id`;

CREATE TABLE `omart_recruitment_apply` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `recruitment_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_recruitment',
  `fullname` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `image_url` text COLLATE utf8_unicode_ci NOT NULL,
  `date_of_birth` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `sex` bit(1) NOT NULL DEFAULT b'0' COMMENT '0 = Unknown, 1 = Male, 2 = Female',
  `phone_number` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `district` int(11) DEFAULT NULL COMMENT 'Reference to district Table',
  `province` int(11) DEFAULT NULL COMMENT 'Reference to province Table',
  `ward` int(11) DEFAULT NULL COMMENT 'Reference to ward Table',
  `street` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `experience_level` int(11) DEFAULT NULL,
  `education_level` int(11) DEFAULT NULL,
  `education_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `language` int(11) DEFAULT NULL,
  `salary_expected` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8_unicode_ci,
  `created_by` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `omart_recruitment_position` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `name_en` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `omart_recruitment_position_level` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `position_id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `name_en` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `omart_recruitment_district_follow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `province` int(11) DEFAULT '0',
  `district` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `omart_recruitment_position_follow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `position_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_category',
  `user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
