SELECT * FROM omart_db.omart_poi_picture p where p.image_url is null;

ALTER TABLE `omart_db`.`omart_poi_picture` 
CHANGE COLUMN `image_url` `image_url` JSON NULL DEFAULT NULL;

