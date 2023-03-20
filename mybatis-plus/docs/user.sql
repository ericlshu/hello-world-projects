CREATE TABLE `tb_user`
(
    `id`        bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_name` varchar(20) NOT NULL COMMENT '用户名',
    `password`  varchar(20) NOT NULL COMMENT '密码',
    `name`      varchar(30) DEFAULT NULL COMMENT '姓名',
    `age`       int(11)     DEFAULT NULL COMMENT '年龄',
    `email`     varchar(50) DEFAULT NULL COMMENT '邮箱',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = UTF8MB4;

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
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(11, 'Kendra', 'opZ8ru53YMi047V8WkID', '鄂书语', 39, 'p9vq8z79rv@live.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(12, 'Clarissa', 'TUMy77QV6KhG37MCGQ7D', '赖琨', 30, '57j62k0z4v@live.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(13, 'Morris', '634cyh41OLo45NzqJfoY', '皮英', 24, '7jkld4g76z1@yandex.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(14, 'Estella', 'HicYiVofBR0UkYgMF2g7', '侯风', 23, 'yzh48tia9cj@263.net');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(15, 'Julian', 'V11Tx84U6LUzz30BwU3E', '嵺舒畅', 37, 'sh161g9bc82w53z@aol.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(16, 'Luis', 'jaSvNG64H3YRK0B2odlK', '蓝子欣', 24, '2u99tai2@aliyun.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(17, 'Liz', 'Pq0el6CB00sL3H6ctquf', '郦德馨', 30, '2507v4r808u1b4x@ask.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(18, 'Etta', '6blX96jKdI5Hqh5F1gH6', '淳于文杰', 27, 'n958iq0xs9t741@hknet.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(19, 'Everett', 'r3oiNV8q2Mkk0pW8HDdl', '戚霄', 35, 'k6nwivwl7@163.net');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(20, 'Celeste', '6D2q85cFi267H5e8QV2C', '桓德茂', 25, '41p2h3x@aliyun.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(21, 'Charles', '6Kb0cM1B9UTUrbAUNPvJ', '巩吉星', 28, '2jx7b65gf7s8lp@hknet.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(22, 'Timothy', 'Z82zTmh0GX9R39vin22t', '乐燕桦', 28, 'lwjz86s38ytr@netvigator.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(23, 'Fernando', 'h1W242pR1VpMnMwvqQ5o', '昌家宜', 34, 'ylllz29zc1a@gmail.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(24, 'Ester', 'h0P27R2ambipi9n4SsbY', '甘婉然', 26, 'x8u0d7n47@gmail.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(25, 'Maurice', '4IusJfi1o396svgLYN6v', '董馨荣', 25, 'tjyp88gdw5@live.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(26, 'Henrietta', 'ccT5l3A6BdpS7wgr858h', '百里炎', 33, 'kp1q4f1k7904l@comcast.net');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(27, 'Greg', 'YEy34yJiTKk37CkrXL2m', '潘畅然', 31, 'p1f37w7gm@yahoo.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(28, 'Larry', 'Ck82TL2tfQ13np0c1WFx', '贺立', 37, 'c10lutp43lt@aliyun.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(29, 'Gertrude', 'nWLt9G93KU5bQ9kcFXHR', '廖文杰', 28, 'vw5403al493i@ctimail.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(30, 'Clara', 'phWQJgicDvy9AMu5Wzn7', '扶仙韵', 22, 'dpiw9l1h7@xfinity.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(31, 'Neil', 'dyv7vagE7XXfRGl4057i', '任玟玉', 30, 'p78um6y54xf8r6a@protonmail.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(32, 'Sam', 'Z5HsLUD0XF5bugFiYh1o', '昌玉', 27, 'a3dm0882@163.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(33, 'Clarice', '3TSawYndvWLycKCEa0Jm', '缪开霁', 22, 'ovoy16054y2ceo@xfinity.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(34, 'Steven', 'O6GT0Kj709Ae784Knp1s', '景绍辉', 33, 'cc3ym3112l@yahoo.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(35, 'Carl', 'ZgCSzN2aJQbLC0BFcszV', '苍伟祺', 39, 'c0c68t1jvmffiq@zoho.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(36, 'Jay', 'WZW8Mt15KngL7QtBH5GK', '迟煜祺', 38, '8km0yi6762i9@sina.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(37, 'Leo', 'GGd6JEO5fYy9m6U04pRZ', '訾兴', 27, 'e23kyzuzro@comcast.net');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(38, 'Velma', 'Q3kgz1YlLWoQ5oGv1X98', '司空韶容', 40, 'pg03gdio@aliyun.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(39, 'Johanna', '7tUZJsV02H4tZ396pgr9', '西门胥豪健', 38, '3622pgb9bd653ou@yandex.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(40, 'Henry', 'y4CGHJ4i7Tt45f5zEWLz', '桂弘文', 25, 'jq3878e3qj8v@netvigator.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(41, 'Miranda', 'hykPfwfulV26oXZ7j5zl', '空高俊', 30, 't4879qaz5r191@msn.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(42, 'Neil', 'wXV6cAkw81euVflNrrox', '闻月怡', 36, 'r61851r9btsno@protonmail.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(43, 'Tania', 'X5V8coW6Jx9saQakPle4', '管帅', 33, 'kmdo3bq@163.net');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(44, 'Paul', '7HVWTv9psp9Sdmv524iz', '索柔婉', 30, 're6k9nhgeex4e@aol.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(45, 'Alexander', 'Yij8bTXrLg3NH74ptv3h', '富智敏', 40, '755o7r3d381i@263.net');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(46, 'Stacey', 'h5p727x676U9ve2tCm7v', '扈鹏程', 25, '24f49ujg@163.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(47, 'Amparo', 'uMn145mvZ1MCCFvLclKG', '聂康', 36, 'm2438q66s66@ask.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(48, 'Jolene', '0Q8DYg0tfy08VVEUHeRj', '衡梓洁', 39, 'n02tttpqu59@yahoo.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(49, 'Theresa', 't8w9Pdz6UBsL1NKZWA24', '车梦秋', 32, '87or3x22d7@163.net');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(50, 'Peggy', 'Wav9s7w8fjTO2Dy8Uctq', '顾熙阳', 27, '9gla1h982a2x@att.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(51, 'Pearl', 'aiv9mWub75Hp93p68Jlq', '益明燦', 24, '8950ph1@verizon.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(52, 'Gordon', 'b1b3ErRma1zjAp5a8wza', '第五妍歌', 20, 'k9882yr30@aliyun.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(53, 'Perry', '8V70L1P01Pr8uOIbR9oG', '容弘雅', 30, 'eppxg07e6@hknet.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(54, 'Mitzi', 'yISB3FbkcS1650uyM3qi', '花杏儿', 35, '748ycjp@msn.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(55, 'Arnold', '2j0r2fF8o99k22l3e0Jg', '越俊智', 24, 'cy67t4jzq7ch49d@163.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(56, 'Calvin', '8kWkegO5wG43E2ac364i', '池俊豪', 26, '9qkvu3q@zoho.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(57, 'Leon', 'HlBdx0Rk5y0gg93tr78x', '郦雪曼', 35, 'lk4rob5vqnky3f4@163.net');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(58, 'Serena', 'ld50SkC9fUF8H20764UL', '海霞姝', 29, '6k0itc17db7vm1@yahoo.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(59, 'Brent', 'mPRN4WhRjbdk1raCF04C', '张嘉惠', 28, 's0sa33k@qq.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(60, 'Rae', 'Cstxb0xP05G3Q5xX58rm', '谈卓', 20, '0f03t01e4dmp@gmail.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(61, 'Jimmie', 'BRViZToJJ75b3wwiyo53', '须雨', 22, 'bt0p4k7njek3t@netvigator.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(62, 'Roy', 'zw2r63lRSUJLzB7c7H79', '鄂玟丽', 27, '64a701mkok9f75@hongkong.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(63, 'Rosanne', 'Iddi3510bo69kyOlucyX', '秋雄强', 37, 'vz21e0179ei0h28@zook.pub');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(64, 'Terrance', 'cp5YTWK9RRrH1OH0MfbZ', '向俊哲', 24, '5h902c91nsp25@gmail.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(65, 'Rowena', '3Q67ah3xeVIg519Xw199', '龙书桃', 24, '3x8z3x693@aliyun.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(66, 'Johnnie', '2JjwQ3l2M01N9U6AFg0S', '蓟松雪', 29, '018uu7yr68@icloud.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(67, 'Willa', '8oZ3ZmM2C87596ROmxFY', '阙祥', 21, '8ko8b95350@yahoo.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(68, 'Frederick', 'e28USjBUHSjiOdc0zci8', '宦浓绮', 36, 't5a27r86m1656ue@yahoo.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(69, 'Virgie', 'u0ydh3E2Ruqo4BQVpQkh', '包璋', 29, 'ab2gqyna3b170mh@hknet.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(70, 'Zachary', '74210Qdh1qKNOy4hGHEH', '侯娴雅', 26, '491jdu3r1iyx3p3@verizon.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(71, 'Renee', '9o0q3S9iCC9AQ00FIUxh', '叶有成', 32, 'c42h2c5klr4ms8r@yahoo.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(72, 'Dianne', 'T5D3RmQEHf9u6Da7cO3j', '钭伟瀚', 29, '4631249w1e0lx@verizon.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(73, 'Eddie', 'mWUsM2UqFwaz2oNih42m', '乌知', 24, '3az65k01615@outlook.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(74, 'Danielle', 'lUWHvUY80n3AVDBsfu67', '廖熠彤', 32, 'xxc7ttm4y@163.net');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(75, 'Derek', '90ONa39p76ChF0B48kxU', '巫马思娜', 22, 've7lw94u356x2@yahoo.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(76, 'Sheri', 'AMDF4P0yiSDmqzdPO129', '须妍和', 32, 'p4p47s33q05@qq.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(77, 'Jeanie', 'E7jEi77sapWq61op0eb5', '郗广', 24, '41f9t526tzs6le@sina.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(78, 'Blanche', 'q06yq65mJn0230o66wez', '琴绮梦', 32, '1a2sk74fy51cr@live.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(79, 'Adrian', '4w80xd46glmR00wJt3bF', '嵺之桃', 37, 'k09h1d249oq34r@zook.pub');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(80, 'Glen', 'H34dn14xG84hHdXRi2W6', '云威', 24, 'hamx154lkw8e933@icloud.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(81, 'Norman', '41WfY2nB0MIf9GYic3TQ', '栗琳怡', 24, '583749a17sreb@aliyun.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(82, 'Oscar', 'M8P8tr46kecq4174B7z5', '司马林', 22, 'nj3u7rlq133@gmail.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(83, 'Samantha', 'JgRGw551xtIxEN7r51sE', '归真一', 33, 'fcdd41449@netvigator.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(84, 'Lola', 'Y8M0Lx4Z733YuQ7H4w3a', '仲珲', 28, '8i09mbhv5@verizon.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(85, 'Pedro', '2Gfn9R6ZD6KRUsqlK6gc', '漕俊朗', 33, '44087q0ug68n30f@263.net');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(86, 'Kenya', '3Y4Gek60zVZfqYnzr4z8', '向正', 32, 'dr83773945gw33@foxmail.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(87, 'Petra', 'Wet33DLdeejA3dEhpl2H', '景鑫', 20, 'ntp3s6jem3n9x@ctimail.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(88, 'Bonnie', '6Oe6DiavCaO8s275J8TU', '崔柔谨', 30, 'dxtqs8yq0ybnz@icloud.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(89, 'Clinton', 'rtT3814e0yR0nuw7G7yl', '昝自光', 26, '56h09c952l77@263.net');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(90, 'Ernest', '7e0VF93tw7v27w7Eod1t', '屠锦', 30, '23hm8503n6ki82@aliyun.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(91, 'Byron', 'adC5gR2IS6G7NaO2Px6u', '尉迟璋', 21, '3n1227n@verizon.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(92, 'Rena', '3wdgT74d1l970jbs0Oee', '官楠楠', 21, '7497x7zw7l39ez2@hotmail.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(93, 'Dolly', '3Gw63kKU2n6th3QU1ROa', '尤明诚', 40, '09osrf8lm@aol.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(94, 'Vilma', 'Wcv73NYJ1ZbBr46lfW6v', '督岩', 31, '51m6c5qu9414@hotmail.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(95, 'Jeffrey', 'Wwfe5KFP9F7R2bxe608p', '贲瑜然', 34, 'v94593zpl0@zoho.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(96, 'Roberto', 'A77V92Sv159D9ROquGaS', '衣弘文', 36, 'yprm9m6q0q3@qq.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(97, 'Beatriz', 'Sgs9uxYM8z8O1weXvyWx', '宦怡嘉', 26, 'z2gv3lb18axebod@hongkong.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(98, 'Chrystal', '51GHa2vYBLH84YQf96Y9', '颛孙泽雨', 20, 'bvn5g3y083jtbv7@yandex.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(99, 'Elma', 'aPu25J53Oc7zSh9Q6mxv', '苟俊豪', 29, 'fm3x35n33@yahoo.com');
INSERT INTO test.tb_user (id, user_name, password, name, age, email) VALUES(100, 'Molly', 'r37H2APGW34K4v77TCGy', '隗淑兰', 28, '1kbfsl4k9@protonmail.com');
