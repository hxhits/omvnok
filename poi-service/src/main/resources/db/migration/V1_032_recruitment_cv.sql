ALTER TABLE `omart_db`.`omart_recruitment_apply` 
ADD COLUMN `status` INT(2) NULL DEFAULT 0 COMMENT '0: New Apply\n1: Interview Invite\n2: Interview Accept\n3: Interview Reject\n4: Interview Fail\n5: Interview OK' AFTER `is_deleted`;

CREATE TABLE `omart_user_cv` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `id_card_number` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `id_card_date` timestamp NULL DEFAULT NULL,
  `id_card_place` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
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
  `description` text COLLATE utf8_unicode_ci,
  `created_by` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `omart_user_cv_notification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `poi_id` int(11) NOT NULL,
  `apply_id` int(11) NOT NULL,
  `title` text COLLATE utf8_unicode_ci,
  `content` text COLLATE utf8_unicode_ci,
  `created_by` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
ALTER TABLE `omart_db`.`omart_user_cv_notification` 
ADD COLUMN `type` INT(1) NULL DEFAULT 0 COMMENT '0: Poi -> User\n1: User -> Poi\n' AFTER `content`;
ALTER TABLE `omart_db`.`omart_user_cv_notification` 
ADD COLUMN `is_deleted` INT(1) NULL DEFAULT 0 AFTER `updated_at`;

ALTER TABLE `omart_db`.`omart_user_cv_notification` 
ADD COLUMN   `status` int(2) DEFAULT '0';

ALTER TABLE `omart_db`.`omart_user_cv_notification` 
ADD COLUMN   `notification_type`  int(1) DEFAULT 0 COMMENT '0 invite interview, 1 invite working , 2 interview fail';

