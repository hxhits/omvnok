CREATE DEFINER=`omart`@`%` PROCEDURE `geodistance_poi`(IN orig_latitude double, IN orig_longitude double, IN radius int , IN paging int, IN size int)
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
    omart_db.omart_point_of_interest p
WHERE
    p.longitude BETWEEN lon1 AND lon2
        AND p.latitude BETWEEN lat1 AND lat2
HAVING distance < radius
ORDER BY distance ASC , p.created_at ASC
LIMIT PAGING , SIZE; 

END