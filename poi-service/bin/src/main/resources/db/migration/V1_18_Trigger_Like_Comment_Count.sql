  `like_count` int(6) DEFAULT '0',
  `comment_count` int(6) DEFAULT '0',
  
  ALTER TABLE `omart_db`.`omart_point_of_interest` 
DROP COLUMN `comment_count`,
DROP COLUMN `like_count`;
------------------------------------------------------------------------------------------
CREATE DEFINER=`omart`@`%` PROCEDURE `geodistance_poi3`(IN orig_latitude double, IN orig_longitude double, IN radius int , IN paging int, IN size int)
BEGIN
  
declare lon1 float;  
declare lon2 float; 
declare lat1 float; 
declare lat2 float; 
-- select longitude, latitude into mylon, mylat from users where id=userid limit 1; 
-- calculate lon and lat for the rectangle: 
set lon1 = orig_longitude-radius/abs(cos(radians(orig_latitude))*69); 
set lon2 = orig_longitude+radius/abs(cos(radians(orig_latitude))*69); 
set lat1 = orig_latitude-(radius/69);  
set lat2 = orig_latitude+(radius/69);
-- run the query: 
SELECT 
     p.id as id ,p.name as name,p.description as description,p.avatar_image as avatarImage,p.cover_image as coverImage,p.own_id as ownerId,p.open_hour as openHour,p.close_hour as closeHour,p.opening_state as isOpening,
			 
    3956 * 2 * ASIN(SQRT(POWER(SIN((orig_latitude - p.latitude) * PI() / 180 / 2),
                            2) + COS(orig_latitude * PI() / 180) * COS(p.latitude * PI() / 180) * POWER(SIN((orig_longitude - p.longitude) * PI() / 180 / 2),
                            2))) AS distance
FROM
    omart_db.omart_point_of_interest p
WHERE
    p.longitude BETWEEN lon1 AND lon2
        AND p.latitude BETWEEN lat1 AND lat2
HAVING distance < radius
ORDER BY distance ASC , p.created_at ASC
LIMIT PAGING , SIZE; 

END
------------------------------------------------------------------------------------------
CREATE DEFINER=`omart`@`%` PROCEDURE `geodistance_poi_enhance`(IN orig_latitude double, IN orig_longitude double, IN radius int , IN paging int, IN size int,IN user_id varchar(2550))
BEGIN
  
declare lon1 float;  
declare lon2 float; 
declare lat1 float; 
declare lat2 float; 
-- select longitude, latitude into mylon, mylat from users where id=userid limit 1; 
-- calculate lon and lat for the rectangle: 
set lon1 = orig_longitude-radius/abs(cos(radians(orig_latitude))*69); 
set lon2 = orig_longitude+radius/abs(cos(radians(orig_latitude))*69); 
set lat1 = orig_latitude-(radius/69);  
set lat2 = orig_latitude+(radius/69);
-- run the query: 
SELECT 
    p.*,
    3956 * 2 * ASIN(SQRT(POWER(SIN((orig_latitude - p.latitude) * PI() / 180 / 2),
                            2) + COS(orig_latitude * PI() / 180) * COS(p.latitude * PI() / 180) * POWER(SIN((orig_longitude - p.longitude) * PI() / 180 / 2),
                            2))) AS distance
FROM
    (SELECT paa.action_type, pp.* FROM omart_db.omart_point_of_interest pp 
LEFT JOIN  (SELECT *FROM omart_db.omart_poi_action pa WHERE pa.action_type = 1 AND pa.user_id = user_id) as paa
ON pp.id = paa.poi_id) as p
WHERE
    p.longitude BETWEEN lon1 AND lon2
        AND p.latitude BETWEEN lat1 AND lat2
HAVING distance < radius
ORDER BY distance ASC , p.created_at ASC
LIMIT PAGING , SIZE; 

END
--------------------------------------------------------------------------------------------
CREATE DEFINER=`omart`@`%` PROCEDURE `geodistance_news_enhance`(IN orig_latitude double, IN orig_longitude double, IN radius int , IN paging int, IN size int,IN approval tinyint(1))
BEGIN
-- declare mylon double;  
-- declare mylat double;  
declare lon1 float;  
declare lon2 float; 
declare lat1 float; 
declare lat2 float; 
-- get the original lon and lat for the userid 
-- select longitude, latitude into mylon, mylat from users where id=userid limit 1; 
-- calculate lon and lat for the rectangle: 
set lon1 = orig_longitude-radius/abs(cos(radians(orig_latitude))*69); 
set lon2 = orig_longitude+radius/abs(cos(radians(orig_latitude))*69); 
set lat1 = orig_latitude-(radius/69);  
set lat2 = orig_latitude+(radius/69);
-- run the query: 
SELECT 
     n.id,n.title,n.description,n.thumbnail_url,n.banner_url,n.news_type,n.is_read,n.own_id,n.longitude,n.latitude,n.poi_id,
    
    3956 * 2 * ASIN(SQRT(POWER(SIN((orig_latitude - n.latitude) * PI() / 180 / 2),
                            2) + COS(orig_latitude * PI() / 180) * COS(n.latitude * PI() / 180) * POWER(SIN((orig_longitude - n.longitude) * PI() / 180 / 2),
                            2))) AS distance
FROM

(SELECT nn.*,p.own_id FROM omart_db.omart_news nn LEFT JOIN  omart_db.omart_point_of_interest p 
ON nn.poi_id = p.id WHERE nn.approval = 1 AND nn.news_type = 1) AS n

WHERE
    n.approval = approval
        AND n.longitude BETWEEN lon1 AND lon2
        AND n.latitude BETWEEN lat1 AND lat2
HAVING distance < radius
ORDER BY n.news_type ASC , distance ASC , n.created_at ASC
LIMIT paging , size; 
-- (paging*size,size)
END
-------------------------------------------------------------------------------------------
DELIMITER //

CREATE TRIGGER omart_poi_action_after_insert
AFTER INSERT
   ON omart_poi_action FOR EACH ROW
BEGIN
  UPDATE omart_point_of_interest SET omart_point_of_interest.like_count = omart_point_of_interest.like_count + 1       
  WHERE omart_point_of_interest.id = NEW.poi_id; 
END; //

DELIMITER ;
--------------------------------------------------------------------------------------------
DROP TRIGGER omart_poi_action_after_insert;


DELIMITER //

CREATE TRIGGER omart_poi_action_after_update
AFTER UPDATE ON omart_poi_action FOR EACH ROW
BEGIN

IF(NEW.action_type = 1) THEN
	 UPDATE omart_point_of_interest SET omart_point_of_interest.like_count = omart_point_of_interest.like_count + 1       
	 WHERE omart_point_of_interest.id = NEW.poi_id; 
ELSE IF(NEW.action_type = 2) THEN
	 UPDATE omart_point_of_interest SET omart_point_of_interest.like_count = omart_point_of_interest.like_count - 1       
	 WHERE omart_point_of_interest.id = NEW.poi_id;
END IF;
END IF;
END; //

DELIMITER ;

DROP TRIGGER omart_poi_action_after_update;
-------------------------------------------------------------------------------------------
DELIMITER //

CREATE TRIGGER omart_poi_comment_after_insert
AFTER INSERT
   ON omart_poi_comment FOR EACH ROW
BEGIN
  UPDATE omart_point_of_interest SET omart_point_of_interest.comment_count = omart_point_of_interest.comment_count + 1       
  WHERE omart_point_of_interest.id = NEW.poi_id; 
END; //

DELIMITER ;
------------------------
     ALTER TABLE `omart_db`.`omart_point_of_interest`
    ADD COLUMN  `poi_state` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0 = open, 1 = close, 2 = pause';