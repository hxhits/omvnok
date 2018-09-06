ALTER TABLE `omart_db`.`omart_timeline` 
CHANGE COLUMN `is_moment` `is_moment` BIT(1) NOT NULL DEFAULT b'0' COMMENT '0 = false , 1 = true (LOVE-HIDE)' ,
ADD COLUMN `moment_radius` INT(11) NULL DEFAULT 5000 AFTER `is_moment`;

DELIMITER $$
CREATE DEFINER=`omart`@`%` PROCEDURE `geodistance_timeline_v4`(IN org_user_id text, IN orig_types VARCHAR(3000), IN orig_latitude double, IN orig_longitude double, IN radius int , IN paging int, IN size int)
BEGIN

declare lon1 float;
declare lon2 float;
declare lat1 float;
declare lat2 float;

set lon1 = orig_longitude-radius/abs(cos(radians(orig_latitude))*69);
set lon2 = orig_longitude+radius/abs(cos(radians(orig_latitude))*69);
set lat1 = orig_latitude-(radius/69);
set lat2 = orig_latitude+(radius/69);

SELECT 
	t.id as id, t.user_id as userId, t.images as images, t.description as description, t.timeline_type as timelineType,
	t.latitude as latitude, t.longitude as longitude, t.created_at as createdAt, t.like_count as likeCount, t.comment_count as commentCount,
	t.font_size as fontSize, t.font_style as fontStyle, t.color as color, t.href as href, t.href_title as hrefTitle,
	t.is_deleted as isDeleted, t.custom_user_avatar as customUserAvatar, t.custom_user_name as customUserName,
	t.updated_at as updatedAt, t.is_privated as isPrivated,t.is_moment as isMoment, t.moment_radius as momentRadius
FROM omart_db.omart_timeline t
WHERE (orig_types = 'ALL' OR FIND_IN_SET(t.timeline_type,orig_types))
	AND t.is_deleted = 0
    AND t.is_moment = 0
	AND (t.is_privated = false OR t.user_id = org_user_id COLLATE utf8_unicode_ci)
	AND (orig_longitude = 0.0 OR t.longitude BETWEEN lon1 AND lon2)
	AND (orig_latitude = 0.0 OR t.latitude BETWEEN lat1 AND lat2)
ORDER BY t.created_at DESC
LIMIT PAGING , SIZE;

END$$
DELIMITER ;


DELIMITER $$
CREATE DEFINER=`omart`@`%` PROCEDURE `geodistance_my_timeline_v3`(IN org_user_id text, IN orig_types VARCHAR(3000), IN orig_latitude double, IN orig_longitude double, IN paging int, IN size int)
BEGIN
declare lon1 float;
declare lon2 float;
declare lat1 float;
declare lat2 float;

SELECT 
	t.id as id, t.user_id as userId, t.images as images, t.description as description, t.timeline_type as timelineType,
	t.latitude as latitude, t.longitude as longitude, t.created_at as createdAt, t.like_count as likeCount, t.comment_count as commentCount,
	t.font_size as fontSize, t.font_style as fontStyle, t.color as color, t.href as href, t.href_title as hrefTitle,
	t.is_deleted as isDeleted, t.custom_user_avatar as customUserAvatar, t.custom_user_name as customUserName,
	t.updated_at as updatedAt, t.is_privated as isPrivated, t.is_moment as isMoment, t.moment_radius as momentRadius
FROM omart_db.omart_timeline t 
WHERE (orig_types = 'ALL' OR FIND_IN_SET(t.timeline_type,orig_types))
	AND t.is_deleted = 0
    AND t.is_moment = 1
    AND t.timeline_type = 1
	AND t.user_id = org_user_id COLLATE utf8_unicode_ci 
	AND (t.longitude BETWEEN (orig_longitude-(t.moment_radius * 0.00062137)/abs(cos(radians(orig_latitude))*69)) AND (orig_longitude+(t.moment_radius * 0.00062137)/abs(cos(radians(orig_latitude))*69))) 
	AND (t.latitude BETWEEN (orig_latitude-((t.moment_radius * 0.00062137)/69)) AND (orig_latitude+((t.moment_radius * 0.00062137)/69))) 
ORDER BY t.created_at DESC 
LIMIT PAGING , SIZE;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`omart`@`%` PROCEDURE `geodistance_my_timeline_v2`(IN org_user_id text, IN orig_types VARCHAR(3000), IN orig_latitude double, IN orig_longitude double, IN radius int , IN paging int, IN size int)
BEGIN

declare lon1 float;
declare lon2 float;
declare lat1 float;
declare lat2 float;

set lon1 = orig_longitude-radius/abs(cos(radians(orig_latitude))*69);
set lon2 = orig_longitude+radius/abs(cos(radians(orig_latitude))*69);
set lat1 = orig_latitude-(radius/69);
set lat2 = orig_latitude+(radius/69);

SELECT 
	t.id as id, t.user_id as userId, t.images as images, t.description as description, t.timeline_type as timelineType,
	t.latitude as latitude, t.longitude as longitude, t.created_at as createdAt, t.like_count as likeCount, t.comment_count as commentCount,
	t.font_size as fontSize, t.font_style as fontStyle, t.color as color, t.href as href, t.href_title as hrefTitle,
	t.is_deleted as isDeleted, t.custom_user_avatar as customUserAvatar, t.custom_user_name as customUserName,
	t.updated_at as updatedAt, t.is_privated as isPrivated, t.is_moment as isMoment, t.moment_radius as momentRadius
FROM omart_db.omart_timeline t 
WHERE (orig_types = 'ALL' OR FIND_IN_SET(t.timeline_type,orig_types))
	AND t.is_deleted = 0
    AND t.is_moment = 1
    AND t.timeline_type = 1
	AND t.user_id = org_user_id COLLATE utf8_unicode_ci 
	AND (t.longitude BETWEEN lon1 AND lon2) 
	AND (t.latitude BETWEEN lat1 AND lat2) 
ORDER BY t.created_at DESC 
LIMIT PAGING , SIZE;

END$$
DELIMITER ;
