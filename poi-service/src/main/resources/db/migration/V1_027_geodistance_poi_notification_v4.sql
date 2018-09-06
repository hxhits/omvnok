DELIMITER $$
CREATE DEFINER=`omart`@`%` PROCEDURE `geodistance_poi_notification_v4`(IN orig_latitude double, IN orig_longitude double, IN radius int , IN paging int, IN size int, IN categoryIds VARCHAR(3000), IN positionLevelIds VARCHAR(3000),IN provinceId int,IN districtIds VARCHAR(3000),IN notificationType int, IN userId VARCHAR(255))
BEGIN
  
declare lon1 float;  
declare lon2 float; 
declare lat1 float; 
declare lat2 float; 

-- run the query: 
if(orig_latitude > 0 and orig_longitude > 0) then

-- calculate lon and lat for the rectangle: 
set lon1 = orig_longitude-radius/abs(cos(radians(orig_latitude))*69); 
set lon2 = orig_longitude+radius/abs(cos(radians(orig_latitude))*69); 
set lat1 = orig_latitude-(radius/69);  
set lat2 = orig_latitude+(radius/69);

SELECT 
   distinct pn.id as id,pn.poi_id as poiId,pn.cat_id as catId,pn.user_id as userId,pn.description as description,pn.images as images ,pn.notification_type as notificationType,pn.created_at as createdAt,
    pn.avatar_image as avatarImages,pn.name as name,pn.address as address,
    3956 * 2 * ASIN(SQRT(POWER(SIN((orig_latitude - pn.latitude) * PI() / 180 / 2),
                            2) + COS(orig_latitude * PI() / 180) * COS(pn.latitude * PI() / 180) * POWER(SIN((orig_longitude - pn.longitude) * PI() / 180 / 2),
                            2))) AS distance,
	pn.phone as phone, pn.latitude as latitude, pn.longitude as longitude, pn.recruit_id as recruitId,  
    pn.color as color, pn.font_style as fontStyle, pn.font_size as fontSize,pn.updated_at as updatedAt,
    pn.bookcar_id as bookcarId, p.own_id as poiOwnerId
FROM
    omart_db.omart_poi_notification pn
LEFT JOIN omart_db.omart_point_of_interest p  
ON p.id = pn.poi_id 
LEFT JOIN omart_db.omart_recruitment r 
ON r.id = pn.recruit_id
WHERE (categoryIds = 'ALL' OR FIND_IN_SET(pn.cat_id,categoryIds)) 
		AND (positionLevelIds = 'ALL' OR FIND_IN_SET(pn.pos_id,positionLevelIds)) 
		AND (districtIds = 'ALL' OR FIND_IN_SET(p.district,districtIds) OR FIND_IN_SET(pn.district,districtIds))
		AND (provinceId = 0 OR p.province = provinceId OR pn.province = provinceId)
		AND (p.is_deleted = false OR pn.poi_id is null)
        AND (r.is_deleted = 0 OR r.is_deleted IS NULL)
        AND (r.state = 0 OR r.state IS NULL)
        AND pn.is_active = 1
		AND pn.longitude BETWEEN lon1 AND lon2
        AND pn.latitude BETWEEN lat1 AND lat2
        AND (pn.notification_type = notificationType OR notificationType = 99)
        ##AND pn.user_id = userId COLLATE utf8_unicode_ci 
HAVING distance < radius
ORDER BY distance ASC , pn.id Desc
LIMIT PAGING , SIZE; 
else #------------------------------else--------------------------------
SELECT 
    pn.id as id,pn.poi_id as poiId,pn.cat_id as catId,pn.user_id as userId,pn.description as description,pn.images as images ,pn.notification_type as notificationType,pn.created_at as createdAt,
    pn.avatar_image as avatarImages,pn.name as name,pn.address as address,
    0 as distance,
	pn.phone as phone, pn.latitude as latitude, pn.longitude as longitude, pn.recruit_id as recruitId,  
    pn.color as color, pn.font_style as fontStyle, pn.font_size as fontSize,pn.updated_at as updatedAt,
	pn.bookcar_id as bookcarId, p.own_id as poiOwnerId
FROM
    omart_db.omart_poi_notification pn
LEFT JOIN omart_db.omart_point_of_interest p  
ON p.id = pn.poi_id
LEFT JOIN omart_db.omart_recruitment r 
ON r.id = pn.recruit_id    
WHERE (categoryIds = 'ALL' OR FIND_IN_SET(pn.cat_id,categoryIds)) 
		AND (positionLevelIds = 'ALL' OR FIND_IN_SET(pn.pos_id,positionLevelIds)) 
		AND (districtIds = 'ALL' OR FIND_IN_SET(p.district,districtIds) OR FIND_IN_SET(pn.district,districtIds))
		AND (provinceId = 0 OR p.province = provinceId OR pn.province = provinceId)
		AND (p.is_deleted = false OR pn.poi_id is null)
		AND (r.is_deleted = 0 OR r.is_deleted IS NULL)
        AND (r.state = 0 OR r.state IS NULL)
		AND pn.is_active = 1
        AND (pn.notification_type = notificationType OR notificationType = 99)
        ##AND pn.user_id = userId COLLATE utf8_unicode_ci 
HAVING distance < radius
ORDER BY distance ASC , pn.updated_at Desc
LIMIT PAGING , SIZE; 
end if;
END$$
DELIMITER ;
