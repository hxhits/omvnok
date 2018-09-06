ALTER TABLE `omart_db`.`omart_category` 
ADD COLUMN `is_disable` INT(1) NULL DEFAULT NULL COMMENT 'NULL: Enable\n1: Disable' AFTER `unit_price`;
