INSERT IGNORE INTO tb_iam_user(`id`, `username`, `password`, `salt`, `register_time`, `active`, `is_delete`) VALUE
(1, 'superadmin', 'ef2d1f4cc5c516f2c89de844c4861e73', '23fd67871e52', '2010-01-01 00:00:00', 1, 0); -- 初始账号密码: superadmin / superadmin

INSERT IGNORE INTO tb_iam_profile(`user_id`, `avatar`, `nickname`, `photo`, `name`, `gender`, `employee_no`, `phone`, `email`) VALUE
(1, null, '超级管理员', null, '超级管理员', null, null, null, null);

INSERT IGNORE INTO tb_iam_user_role(`user_id`, `role_id`) VALUE
(1, 20001);