ALTER TABLE `omart_db`.`omart_point_of_interest`
ADD COLUMN  `disable_sale_feature` bit(1) NOT NULL DEFAULT 0 COMMENT '0 = enable sale feature, 1 = disable sale feature';

ALTER TABLE `omart_db`.`omart_point_of_interest`
ADD COLUMN  `ring_index` int(2) NOT NULL DEFAULT '0';

ALTER TABLE `omart_db`.`omart_recruitment_position_level`
ADD COLUMN  `title_en` varchar(255) COLLATE utf8_unicode_ci NOT NULL;

ALTER TABLE `omart_db`.`omart_contact` 
CHANGE COLUMN `contact_name` `contact_name` VARCHAR(255) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL DEFAULT '' ;
