CREATE TABLE `omart_driver_call_log` (
  `driver_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `bookcar_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_book_car',
  `booker_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `state` int(2) NOT NULL DEFAULT '0',
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `createa_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`bookcar_id`,`driver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE `omart_driver_car_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `name_en` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `image` text COLLATE utf8_unicode_ci NOT NULL,
  `order` int(11) DEFAULT '20',
  `keywords` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `unit_price` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `keywords` (`keywords`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `omart_driver_dist_follow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `province` int(11) DEFAULT '0',
  `district` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=280 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE `omart_driver_follow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `car_type_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_driver_car_type',
  `user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1609 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE `omart_driver_info` (
  `user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `full_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `avatar` varchar(550) COLLATE utf8_unicode_ci DEFAULT NULL,
  `date_of_birth` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `identity_card` varchar(12) COLLATE utf8_unicode_ci NOT NULL,
  `number_plate` varchar(12) COLLATE utf8_unicode_ci NOT NULL,
  `date_of_registration` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `active_province` int(11) DEFAULT NULL COMMENT 'Reference to province Table',
  `car_type_id` int(11) DEFAULT NULL COMMENT 'Reference to omart_driver_car_type Table',
  `phone_number` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `model` varchar(100) COLLATE utf8_unicode_ci DEFAULT '',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `phone_number_UNIQUE` (`phone_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `omart_driver_location` (
  `user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `car_type_id` int(11) NOT NULL COMMENT 'REFERENCES KEY to omart_driver_car_type',
  `name` varchar(150) COLLATE utf8_unicode_ci DEFAULT '',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `omart_driver_push_token` (
  `user_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `token` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `client` int(2) DEFAULT '0' COMMENT '1 = android,2 = ios, 3 = web',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;



ALTER TABLE `omart_db`.`omart_book_car` 
CHANGE COLUMN `offer_price` `offer_price` INT(11) NOT NULL ,
CHANGE COLUMN `user_price` `user_price` INT(11) NOT NULL ;

----store procedure-----
get_nearest_driver_v1
------------------------
CREATE DEFINER=`omart`@`%` PROCEDURE `get_nearest_driver_v1`(IN orig_latitude double, IN orig_longitude double, IN radius int , IN paging int, IN size int,IN carTypeId int)
BEGIN
  
declare lon1 float;  
declare lon2 float; 
declare lat1 float; 
declare lat2 float; 

-- calculate lon and lat for the rectangle: 
set lon1 = orig_longitude-radius/abs(cos(radians(orig_latitude))*69); 
set lon2 = orig_longitude+radius/abs(cos(radians(orig_latitude))*69); 
set lat1 = orig_latitude-(radius/69);  
set lat2 = orig_latitude+(radius/69);

SELECT 
	l.user_id as userId, l.car_type_id as carTypeId, l.latitude as latitude ,l.longitude as longitude, l.name as name
 --   3956 * 2 * ASIN(SQRT(POWER(SIN((orig_latitude - l.latitude) * PI() / 180 / 2),
  --                          2) + COS(orig_latitude * PI() / 180) * COS(l.latitude * PI() / 180) * POWER(SIN((orig_longitude - l.longitude) * PI() / 180 / 2),
  --                          2))) AS distance	
FROM
    omart_db.omart_driver_location l
WHERE (carTypeId= 0 OR l.car_type_id = carTypeId) AND
l.longitude BETWEEN lon1 AND lon2
        AND l.latitude BETWEEN lat1 AND lat2
-- HAVING distance < radius
 LIMIT PAGING , SIZE;
END