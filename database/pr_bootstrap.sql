/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  rahulkk
 * Created: 17 Jan, 2017
 */

INSERT INTO `sb_users` (`id`, `username`, `first_name`, `last_name`, `email_address`, `mobile_no`, `alt_phone_no`, `password`, `reset_password`, `manager`, `photo`, `gender`, `last_login`, `active`, `created_by`, `last_updated_by`, `comments`, `last_login_from`, `job_title`, `department`, `salt`, `company`) VALUES ('1', 'pradmin@revvster.in',  'PlayRight', 'Admin',  'pradmin@revvster.in', '', '',  'VyebFgw20/SlUCpZmTGLjhaV8lo=',  '0', NULL, NULL, 'Male', NULL,  '1', '1', '1', 'Created by Bootstrap', NULL, 'Administrator',  'Admin', 'rQFcckaTe4U=', NULL );

INSERT INTO `sb_roles` (`raw_name`, `name`) VALUES ('SysAdmin', 'System Administrator'), ('PlayRightUser', 'PlayRight User');

INSERT INTO `sb_entitlements` (`resource`, `action`) VALUES ('analytics', 'list'), ('company', 'add'), ('company', 'delete'), ('company', 'list'), ('company', 'update'), ('company', 'view'), ('configuration', 'update'), ('location', 'add'), ('location', 'delete'), ('location', 'list'), ('location', 'update'), ('location', 'view'), ('project', 'add'), ('project', 'assign_user'), ('project', 'delete'), ('project', 'list'), ('project', 'update'), ('project', 'view'), ('setting', 'add'), ('setting', 'delete'), ('setting', 'list'), ('setting', 'update'), ('setup', 'list'), ('user', 'add'), ('user', 'delete'), ('user', 'device_login'), ('user', 'list'), ('user', 'login_without_company'), ('user', 'reset_password'), ('user', 'update'), ('user', 'view');

INSERT INTO `sb_role_ent_mapping`
(`role`,
`role_name`,
`entitlement`,
`entitlement_name`,
`context`,
`active`)
VALUES
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'company::add'), 'company::add', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'company::update'), 'company::update', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'company::delete'), 'company::delete', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'project::add'), 'project::add', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'project::update'), 'project::update', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'project::delete'), 'project::delete', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'location::add'), 'location::add', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'location::update'), 'location::update', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'location::delete'), 'location::delete', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'user::add'), 'user::add', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'user::update'), 'user::update', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'user::delete'), 'user::delete', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'user::list'), 'user::list', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'user::reset_password'), 'user::reset_password', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'user::login_without_company'), 'user::login_without_company', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'user::view'), 'user::view', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'company::view'), 'company::view', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'project::view'), 'project::view', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'location::view'), 'location::view', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'company::list'), 'company::list', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'project::list'), 'project::list', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'location::list'), 'location::list', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'project::assign_user'), 'project::assign_user', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'configuration::update'), 'configuration::update', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'analytics::list'), 'analytics::list', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'setting::add'), 'setting::add', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'setting::update'), 'setting::update', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'setting::delete'), 'setting::delete', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'setting::list'), 'setting::list', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'System Administrator'), 'System Administrator', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'setup::list'), 'setup::list', 'ALL', '1'),
((select `id` from sb_roles where `name` = 'PlayRight User'), 'PlayRight User', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'user::reset_password'), 'user::reset_password', 'SELF', '1'),
((select `id` from sb_roles where `name` = 'PlayRight User'), 'PlayRight User', (select `id` from sb_entitlements where concat(concat(`resource`, '::'), `action`) = 'analytics::list'), 'analytics::list', 'SELF', '1');


INSERT INTO `sb_roles_context`
(`role_ent`,
`allowed_role`,
`active`)
VALUES
((select `id` from sb_role_ent_mapping where `role_name` = 'System Administrator' and `entitlement_name` = 'user::add'),(select `id` from sb_roles where `name` = 'System Administrator'),'1'),
((select `id` from sb_role_ent_mapping where `role_name` = 'System Administrator' and `entitlement_name` = 'user::update'),(select `id` from sb_roles where `name` = 'System Administrator'),'1'),
((select `id` from sb_role_ent_mapping where `role_name` = 'System Administrator' and `entitlement_name` = 'user::add'),(select `id` from sb_roles where `name` = 'PlayRight User'),'1'),
((select `id` from sb_role_ent_mapping where `role_name` = 'System Administrator' and `entitlement_name` = 'user::update'),(select `id` from sb_roles where `name` = 'PlayRight User'),'1');

INSERT INTO `sb_user_role_mapping`
(`user`,
`role`,
`comments`)
VALUES
((select `id` from sb_users where `username` = 'pradmin@revvster.in'), (select `id` from sb_roles where `name` = 'System Administrator'), 'Do not deactivate or delete');

INSERT INTO `sb_settings`
(`name`,
`parameter`,
`value`,
`description`)
VALUES
('Enable Location', 'EnableLocation', 'false', 'Global switch to enable/disable location context'),
('Max Inactivity Timeout', 'MaxInactiveInterval', '30', 'Inactivity timeout for session to expire in minutes');

