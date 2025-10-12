-- 系统初始角色 --
INSERT IGNORE INTO tb_iam_role(`id`, `identifier`, `name`, `description`, `active`, `sort_id`) VALUE
(200001, 'super_admin', '超级管理员', null, 1, 200001);

-- 系统初始用户 --
INSERT IGNORE INTO tb_iam_user(`id`, `username`, `password`, `salt`, `register_time`, `active`) VALUE
(200001, 'superadmin', '8176126c91f53981449d7575bafa1253', '6386718be1b3', '2025-01-01 00:00:00', 1);   -- superadmin / superadmin
INSERT IGNORE INTO tb_iam_profile(`user_id`, `avatar`, `nickname`, `photo`, `name`, `gender`, `employee_no`, `phone`, `email`, `main_department_id`, `main_position_id`) VALUE
(200001, null, '超级管理员', null, '超级管理员', null, null, null, null, null, null);
INSERT IGNORE INTO tb_iam_user_role(`user_id`, `role_id`) VALUE
(200001, 200001);   -- 超级管理员 <--> 超级管理员
