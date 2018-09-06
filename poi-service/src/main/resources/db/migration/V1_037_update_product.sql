ALTER TABLE `omart_db`.`omart_poi_item` 
ADD COLUMN `out_of_stock` INT(1) NULL DEFAULT 0 AFTER `featured_image`;
