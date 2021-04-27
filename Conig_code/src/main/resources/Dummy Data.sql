delete from action where id>0;
insert into action (id, name) values(1,'USER_REGULARIZED');
insert into action (id, name) values(2,'SYSTEM_REGULARIZED');

delete from action_parameters where id>0;
insert into action_parameters (id,name,value,action_id) values(1,'MESSAGE_ID','1',1);
insert into action_parameters (id,name,value,action_id) values(2,'WAITING_TIME','15',1);
insert into action_parameters (id,name,value,action_id) values(3,'IS_BLOCK','YES',1);

insert into action_parameters (id,name,value,action_id) values(4,'MESSAGE_ID','2',2);
insert into action_parameters (id,name,value,action_id) values(5,'IS_ALLOW','YES',2);

delete from mobile_operator where id>0;
insert into mobile_operator (id,name) values(1,'AIRTEL');
insert into mobile_operator (id,name) values(2,'VODAFONE');
insert into mobile_operator (id,name) values(4,'IDEA');
insert into mobile_operator (id,name) values(3,'BSNL');

delete from sms_script where id>0;
insert into sms_script (id,type,template,action_id) values (1,'NOTIFICATION','Dear user your <IMEI> device is now mapped with <MSISDN>',2);
insert into sms_script (id,type,template,action_id) values (2,'NOTIFICATION','Dear user your <IMEI> device is conflicted. Your action is required. Track Complained with Ticket id <TICKET_ID>',1);
insert into sms_script (id,type,template,action_id) values (3,'REMINDER','We have not received any action against <IMEI> device and Ticket id <TICKET_ID>',1);

delete from rules where id>0;
insert into rules (id,name,description,gui_display,enabled,created_by,approved_by,created_on,modified_on,rule_type) values (1,'NULL','IMEI NOT NULL',0,1,'dummy','dummy',NOW(),NOW(),'STATIC');
insert into rules (id,name,description,gui_display,enabled,created_by,approved_by,created_on,modified_on,rule_type) values (2,'LENGTH','IMEI MUST BE 16 digits',0,1,'dummy','dummy',NOW(),NOW(),'STATIC');
insert into rules (id,name,description,gui_display,enabled,created_by,approved_by,created_on,modified_on,rule_type) values (3,'LUNG ALGO','Check Sum must be ok',0,1,'dummy','dummy',NOW(),NOW(),'STATIC');
insert into rules (id,name,description,gui_display,enabled,created_by,approved_by,created_on,modified_on,rule_type) values (4,'VIP','Device mapping is in VIP LIST',1,1,'dummy','dummy',NOW(),NOW(),'EXCEPTION');
insert into rules (id,name,description,gui_display,enabled,created_by,approved_by,created_on,modified_on,rule_type) values (5,'ALREADY REGULISED','IMEI Already validated',0,1,'dummy','dummy',NOW(),NOW(),'EXCEPTION');
insert into rules (id,name,description,gui_display,enabled,created_by,approved_by,created_on,modified_on,rule_type) values (6,'PENDING REGULISED','IMEI pending for user action for validation',0,1,'dummy','dummy',NOW(),NOW(),'EXCEPTION');
insert into rules (id,name,description,gui_display,enabled,created_by,approved_by,created_on,modified_on,rule_type) values (7,'DUPLICATE','IMEI and msisdn combination should be less than 5',1,1,'dummy','dummy',NOW(),NOW(),'DYNAMIC');
insert into rules (id,name,description,gui_display,enabled,created_by,approved_by,created_on,modified_on,rule_type) values (8,'FOREIGN POLICY','Foreign number allowed for 30 days',1,1,'dummy','dummy',NOW(),NOW(),'DYNAMIC');
insert into rules (id,name,description,gui_display,enabled,created_by,approved_by,created_on,modified_on,rule_type) values (9,'TAX PAID','Tax must be paid of IMEI Device',1,1,'dummy','dummy',NOW(),NOW(),'DYNAMIC');

delete from system_policy_mapping where id>0;
insert into system_policy_mapping (id,period,rule_id,action_id,priority,created_on,modified_on,created_by,modified_by) values(1,'GRACE',4,2,1,NOW(),NOW(),'dummy','dummy');
insert into system_policy_mapping (id,period,rule_id,action_id,priority,created_on,modified_on,created_by,modified_by) values(2,'GRACE',1,2,2,NOW(),NOW(),'dummy','dummy');
insert into system_policy_mapping (id,period,rule_id,action_id,priority,created_on,modified_on,created_by,modified_by) values(3,'GRACE',2,2,3,NOW(),NOW(),'dummy','dummy');
insert into system_policy_mapping (id,period,rule_id,action_id,priority,created_on,modified_on,created_by,modified_by) values(4,'GRACE',3,2,4,NOW(),NOW(),'dummy','dummy');
insert into system_policy_mapping (id,period,rule_id,action_id,priority,created_on,modified_on,created_by,modified_by) values(5,'GRACE',7,2,5,NOW(),NOW(),'dummy','dummy');

insert into system_policy_mapping (id,period,rule_id,action_id,priority,created_on,modified_on,created_by,modified_by) values(6,'POST_GRACE',4,1,1,NOW(),NOW(),'dummy','dummy');
insert into system_policy_mapping (id,period,rule_id,action_id,priority,created_on,modified_on,created_by,modified_by) values(7,'POST_GRACE',5,1,2,NOW(),NOW(),'dummy','dummy');
insert into system_policy_mapping (id,period,rule_id,action_id,priority,created_on,modified_on,created_by,modified_by) values(8,'POST_GRACE',6,1,3,NOW(),NOW(),'dummy','dummy');
insert into system_policy_mapping (id,period,rule_id,action_id,priority,created_on,modified_on,created_by,modified_by) values(9,'POST_GRACE',1,1,4,NOW(),NOW(),'dummy','dummy');
insert into system_policy_mapping (id,period,rule_id,action_id,priority,created_on,modified_on,created_by,modified_by) values(10,'POST_GRACE',2,1,5,NOW(),NOW(),'dummy','dummy');
insert into system_policy_mapping (id,period,rule_id,action_id,priority,created_on,modified_on,created_by,modified_by) values(11,'POST_GRACE',3,1,6,NOW(),NOW(),'dummy','dummy');
insert into system_policy_mapping (id,period,rule_id,action_id,priority,created_on,modified_on,created_by,modified_by) values(12,'POST_GRACE',7,1,7,NOW(),NOW(),'dummy','dummy');
insert into system_policy_mapping (id,period,rule_id,action_id,priority,created_on,modified_on,created_by,modified_by) values(13,'POST_GRACE',8,1,8,NOW(),NOW(),'dummy','dummy');
insert into system_policy_mapping (id,period,rule_id,action_id,priority,created_on,modified_on,created_by,modified_by) values(14,'POST_GRACE',9,1,9,NOW(),NOW(),'dummy','dummy');

delete from vip_list where id>0;
insert into vip_list (id,imei,msisdn,requested_by,approved_by) values (1,2819281928321234,323333333,'dummy','dummy');



delete from sms_account where id>0;
insert into sms_account (id,ip,pass_word,port,status,tps,url,user_name) values (1,'127.0.0.1','jk@123',3232,1,20,'http://127.0.0.1:8888/?to=<TO>&from=<FROM>&text<SMS>','username');
insert into sms_account (id,ip,pass_word,port,status,tps,url,user_name) values (2,'127.0.0.1','jk@123',3232,1,20,'http://127.0.0.1:9090/?to=<TO>&from=<FROM>&text<SMS>','username');
insert into sms_account (id,ip,pass_word,port,status,tps,url,user_name) values (3,'127.0.0.1','jk@123',3232,1,20,'http://127.0.0.1:9091/?to=<TO>&from=<FROM>&text<SMS>','username');

delete from regularized_imei where id>0;
insert into regularized_imei (id,created_on,file_name,imei,imei_status,imsi,modefied_on,msisdn,remarks,failed_rule_id,mobile_operator) 
values (1,NOW(),'Airtel_2019May21',2819281928321234,'OK',324343434434,NOW(),9090909090,'',3,1);
insert into regularized_imei (id,created_on,file_name,imei,imei_status,imsi,modefied_on,msisdn,remarks,failed_rule_id,mobile_operator) 
values (2,NOW(),'Airtel_2019May21',2819281928321235,'NOT_OK',324343434435,NOW(),9090909091,'',9,1);


delete from mediation_source where id>0;
insert into mediation_source (id,file_format,file_name_format , file_path, pooling_frequency,mobile_operator_id) values (1,'csv','<OPERATOR>_<DATE>','/home/ceir/mediationfiles/airtel',3,1);
insert into mediation_source (id,file_format,file_name_format , file_path, pooling_frequency,mobile_operator_id) values (2,'csv','<OPERATOR>_<DATE>','/home/ceir/mediationfiles/vodafone',3,2);
insert into mediation_source (id,file_format,file_name_format , file_path, pooling_frequency,mobile_operator_id) values (3,'csv','<OPERATOR>_<DATE>','/home/ceir/mediationfiles/idea',3,4);
insert into mediation_source (id,file_format,file_name_format , file_path, pooling_frequency,mobile_operator_id) values (4,'csv','<OPERATOR>_<DATE>','/home/ceir/mediationfiles/bsnl',3,3);

#delete from duplicate_imei_msisdn;
insert into duplicate_imei_msisdn (imei,msisdn,created_on,file_name,imei_status,imsi,regulized_by_user,mobile_operator_id) values (2819281928321235,9090909091,NOW(),'Airtel_2019May21','OK',324343434435,1,1);
insert into duplicate_imei_msisdn (imei,msisdn,created_on,file_name,imei_status,imsi,regulized_by_user,mobile_operator_id) values (2819281928321235,9090909092,NOW(),'Airtel_2019May22','OK',324343434436,1,1);
insert into duplicate_imei_msisdn (imei,msisdn,created_on,file_name,imei_status,imsi,regulized_by_user,mobile_operator_id) values (2819281928321235,9090909093,NOW(),'Idea_2019May23','OK',324343434437,1,2);

#delete from pending_actions where ticket_id != null;
insert into pending_actions (ticket_id,created_on,end_datefor_user_action,filename,imei,imsi,modified_on,msisdn,transaction_state,action_id,failed_rule_id,mobile_operator_id) values ('CIER_2342323',NOW(),now()+5,'Airtel_2019May21',2819281928321234,324343434434,NOW(),9090909092,'WAITING_FOR_USER_ACTION',2,7,1);
insert into pending_actions (ticket_id,created_on,end_datefor_user_action,filename,imei,imsi,modified_on,msisdn,transaction_state,action_id,failed_rule_id,mobile_operator_id) values ('CIER_2342324',NOW(),now()+5,'Airtel_2019May21',2819281928321235,324343434435,NOW(),9090909094,'WAITING_FOR_USER_ACTION',5,8,1);
insert into pending_actions (ticket_id,created_on,end_datefor_user_action,filename,imei,imsi,modified_on,msisdn,transaction_state,action_id,failed_rule_id,mobile_operator_id) values ('CIER_2342325',NOW(),now()+5,'Airtel_2019May21',2819281928321236,324343434436,NOW(),9090909095,'WAITING_FOR_USER_ACTION',1,9,1);
insert into pending_actions (ticket_id,created_on,end_datefor_user_action,filename,imei,imsi,modified_on,msisdn,transaction_state,action_id,failed_rule_id,mobile_operator_id) values ('CIER_2342326',NOW(),now()+5,'Airtel_2019May21',2819281928321237,324343434437,NOW(),9090909096,'WAITING_FOR_USER_ACTION',1,1,1);

#delete from user_notification where submit_status='OK';
insert into user_notification (msisdn ,imei,imsi,sms_script_id,rules_id,ticket_id,created_on,submit_status,message,message_type) values (1212321,1231232122321231,132131231233,1,1,'ticket_id-1',NOW(),'OK','Test Message','NOTIFICATION');
insert into user_notification (msisdn ,imei,imsi,sms_script_id,rules_id,ticket_id,created_on,submit_status,message,message_type) values (1212321,1231232122321231,132131231233,1,1,'ticket_id-2',NOW(),'OK','Test Message','REMINDER');
insert into user_notification (msisdn ,imei,imsi,sms_script_id,rules_id,ticket_id,created_on,submit_status,message,message_type) values (1212321,1231232122321231,132131231233,1,1,'ticket_id-3',NOW(),'OK','Test Message','INFO');


#delete from device_snap_shot;
#insert into device_snap_shot (imei,msisdn,created_on,duplicate_count,foregin_rule,global_blacklist,grey_list,imei_status,imsi,lastp_policy_breached,lastp_policy_breached_date,period,remarks,tax_paid,valid_import,mobile_operator_id) 
#values ()
