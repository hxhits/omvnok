ALTER TABLE `omart_db`.`omart_recruitment_position` 
ADD COLUMN `is_disabled` INT(1) NULL DEFAULT 0 AFTER `order`;

ALTER TABLE `omart_db`.`omart_recruitment_position_level` 
ADD COLUMN `is_disabled` INT(1) NULL DEFAULT 0 AFTER `name_en`;

ALTER TABLE `omart_db`.`omart_recruitment_apply` 
CHANGE COLUMN `status` `status` INT(2) NULL DEFAULT '0' COMMENT '0: New Apply\n1: Interview Invite (Sent invitation to Applier)\n2: Interview Accept (from Applier)\n3: Interview Reject (from Applier)\n4: Interview Fail (from Interviewer)\n5: Interview OK (from Interviewer)\n6: Accept Onboard (from Applier)\n7: Reject Onboard (from Applier)' ;
