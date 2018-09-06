ALTER TABLE `omart_db`.`omart_category`
    ADD COLUMN  `name_en` varchar(255) COLLATE utf8_unicode_ci NOT NULL;
    
     ALTER TABLE `omart_db`.`omart_category`
    ADD COLUMN   `sub_description_en` varchar(600) COLLATE utf8_unicode_ci DEFAULT '';
    
     ALTER TABLE `omart_db`.`omart_category`
    ADD COLUMN  `description_en` text COLLATE utf8_unicode_ci;
    
    
    ALTER TABLE `omart_db`.`omart_point_of_interest`
    ADD COLUMN `career` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT '';