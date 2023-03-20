drop table tb_user ;

CREATE TABLE `tb_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_name` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(20) NOT NULL COMMENT '密码',
  `name` varchar(30) DEFAULT NULL COMMENT '姓名',
  `age` int DEFAULT NULL COMMENT '年龄',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(1, 'Lana', 'Y09fnnS6g9q9p6N488nY', '雍鹤梦', 25, '99zw3u4y5@163.net');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(2, 'Beth', 'NE7GzGO3mRYwD3Xk72Gb', '东郭晋鹏', 30, 'wmka87m@yahoo.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(3, 'Gary', 'm46pWY7N3VIIls8c4R0s', '戈峻峰', 31, 'wy0up6ebz0@hongkong.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(4, 'Nora', '9W38Yr27Ejf464fL97Ly', '终熙茂', 39, '7bff3y2@live.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(5, 'Goldie', 'ilI7C9PaLNq5eIU9kr55', '巫荣', 30, '69n0cbo3@yahoo.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(6, 'Jimmy', 'UO47TB40Byv2TF4dDs1a', '阙可儿', 31, 'qiasmpqj14w6l67@qq.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(7, 'Mitchell', '16GNbp6614ceSL08bNQo', '鲜于小雯', 34, 'k5d4vea7w768426@ask.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(8, 'Dwayne', '0gobrIDl2yXR9y5871sq', '项圣杰', 31, 'cf7yt4e0c@gmail.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(9, 'Glenn', 'l21yUC7wZ13khzq7WWR0', '木泰哲', 29, 'z3l39btc38@zoho.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(10, 'Erin', 'no3SiGSwYxcv1S15F5gs', '公孙欣彤', 27, 'n7dp6xatk4@protonmail.com');

select * from tb_user;
