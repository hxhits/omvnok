ALTER TABLE `oauth_user` ADD COLUMN `activation_code` int(11) DEFAULT NULL;
ALTER TABLE `oauth_user` ADD COLUMN `last_sent` BIGINT DEFAULT NULL;
ALTER TABLE `oauth_user` ADD COLUMN `is_verified` BOOLEAN DEFAULT FALSE;