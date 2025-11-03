INSERT IGNORE INTO tb_iam_user(`id`, `username`, `password`, `salt`, `register_time`, `active`, `is_delete`) VALUE
(1, 'superadmin', '8176126c91f53981449d7575bafa1103', '6386718be1b3', '2010-01-01 00:00:00', 1, 0); -- 账号密码: superadmin / superadmin

INSERT IGNORE INTO tb_iam_user_role(`user_id`, `role_id`) VALUE
(1, 20001);