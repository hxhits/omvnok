CREATE DEFINER=`omart`@`%` PROCEDURE `geodistance_news`(IN orig_latitude double, IN orig_longitude double, IN radius int , IN paging int, IN size int,IN approval tinyint(1))
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
-- run the query: 
SELECT 
    n.*,
    3956 * 2 * ASIN(SQRT(POWER(SIN((orig_latitude - n.latitude) * PI() / 180 / 2),
                            2) + COS(orig_latitude * PI() / 180) * COS(n.latitude * PI() / 180) * POWER(SIN((orig_longitude - n.longitude) * PI() / 180 / 2),
                            2))) AS distance
FROM
    omart_db.omart_news n
WHERE
    n.approval = approval
        AND n.longitude BETWEEN lon1 AND lon2
        AND n.latitude BETWEEN lat1 AND lat2
HAVING distance < radius
ORDER BY n.news_type ASC , distance ASC , n.created_at ASC
LIMIT paging , size; 
-- (paging*size,size)
END

-----------updated-----------
CREATE DEFINER=`omart`@`%` PROCEDURE `geodistance_news`(IN orig_latitude double, IN orig_longitude double, IN radius int , IN paging int, IN size int,IN approval tinyint(1))
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
    n.*,
    3956 * 2 * ASIN(SQRT(POWER(SIN((orig_latitude - n.latitude) * PI() / 180 / 2),
                            2) + COS(orig_latitude * PI() / 180) * COS(n.latitude * PI() / 180) * POWER(SIN((orig_longitude - n.longitude) * PI() / 180 / 2),
                            2))) AS distance
FROM
    omart_db.omart_news n
WHERE
    n.approval = approval AND n.news_type = 1
        AND n.longitude BETWEEN lon1 AND lon2
        AND n.latitude BETWEEN lat1 AND lat2
HAVING distance < radius
ORDER BY n.news_type ASC , distance ASC , n.created_at ASC
LIMIT paging , size; 
-- (paging*size,size)
END