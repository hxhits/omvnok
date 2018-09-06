ALTER TABLE `omart_db`.`omart_user_cv_notification`
ADD COLUMN `result` int(2) DEFAULT 0 COMMENT '0: Not yet have result \n11: Pass interview\n10: Fail interview';