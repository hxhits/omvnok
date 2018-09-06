INSERT INTO `omart_db`.`omart_category`(`id`, `name`, `image`, `item_tab_name`, `is_item_detail`) VALUES (1, 'Ăn uống', 'http://via.placeholder.com/300x300', 'Thực đơn', 0);
INSERT INTO `omart_db`.`omart_category`(`id`, `name`, `image`, `item_tab_name`, `is_item_detail`) VALUES (2, 'Đồ nam', 'http://via.placeholder.com/300x300', 'Sản phẩm', 1);
INSERT INTO `omart_db`.`omart_category`(`id`, `name`, `image`, `item_tab_name`, `is_item_detail`) VALUES (3, 'Đồ nữ', 'http://via.placeholder.com/300x300', 'Sản phẩm', 1);
INSERT INTO `omart_db`.`omart_category`(`id`, `name`, `image`, `item_tab_name`, `is_item_detail`) VALUES (4, 'Đồ công nghệ', 'http://via.placeholder.com/300x300', 'Sản phẩm', 1);
INSERT INTO `omart_db`.`omart_category`(`id`, `name`, `image`, `item_tab_name`, `is_item_detail`) VALUES (5, 'Thể thao', 'http://via.placeholder.com/300x300', 'Sản phẩm', 1);
INSERT INTO `omart_db`.`omart_category`(`id`, `name`, `image`, `item_tab_name`, `is_item_detail`) VALUES (6, 'Xe cộ', 'http://via.placeholder.com/300x300', 'Sản phẩm', 1);
INSERT INTO `omart_db`.`omart_category`(`id`, `name`, `image`, `item_tab_name`, `is_item_detail`) VALUES (7, 'Trang sức', 'http://via.placeholder.com/300x300', 'Sản phẩm', 1);
INSERT INTO `omart_db`.`omart_category`(`id`, `name`, `image`, `item_tab_name`, `is_item_detail`) VALUES (8, 'Mẹ & Bé', 'http://via.placeholder.com/300x300', 'Sản phẩm', 1);
INSERT INTO `omart_db`.`omart_category`(`id`, `name`, `image`, `item_tab_name`, `is_item_detail`) VALUES (9, 'Gia dụng', 'http://via.placeholder.com/300x300', 'Sản phẩm', 1);
INSERT INTO `omart_db`.`omart_category`(`id`, `name`, `image`, `item_tab_name`, `is_item_detail`) VALUES (10, 'Đồng hồ', 'http://via.placeholder.com/300x300', 'Sản phẩm', 1);

INSERT INTO `omart_db`.`omart_point_of_interest`(
						`cat_id`, `id`, `name`, `description`, `address`, `ward`, `district`, `province`,
						`phone`, `latitude`, `longitude`, `location`, `open_hour`, `close_hour`, `opening_state`,
						`avatar_image`, `cover_image`, `featured_image`)
			VALUES (1, 1, 'Cơm tấm An Dương Vương', 'abc', '500 An Dương Vương', '4', '5', 'Ho Chi Minh City',
							'1234,5678,9876', 10.755968, 106.666287, NULL, '07:00', '21:30', 1,
							'[{"url":"http://via.placeholder.com/300x300"}]',
							'[{"url":"http://cdn.diachiso.vn/images/600x400/2011/6/7/com-tam-an-duong-vuong.jpg"}]',
							'[{"url":"http://via.placeholder.com/300x300"}]');
INSERT INTO `omart_db`.`omart_point_of_interest`(
						`cat_id`, `id`, `name`, `description`, `address`, `ward`, `district`, `province`,
						`phone`, `latitude`, `longitude`, `location`, `open_hour`, `close_hour`, `opening_state`,
						`avatar_image`, `cover_image`, `featured_image`)
			VALUES (4, 2, 'Điện máy nội thất chợ lớn', 'Siêu thị điện máy chợ lớn, Q5, TpHCM...',
			        'Lô G, Chung cư Hùng Vương', '11', '5', 'Ho Chi Minh City',
							'1234,5678,9876', 10.759628, 106.672469, NULL, '09:30', '22:00', 1,
							'[{"url":"http://via.placeholder.com/300x300"}]',
							'[{"url":"https://dienmaycholon.vn/public/dienmaycholon/general/img/gioi-thieu.jpg"}]',
							'[{"url":"http://via.placeholder.com/300x300"}]');

INSERT INTO `omart_db`.`omart_poi_item`(`poi_id`, `id`, `name`, `description`, `unit_price`,
							`type`, `created_at`, `avatar_image`, `cover_image`, `featured_image`)
			VALUES (1, 1, 'Cơm tấm sườn bì chả', NULL, 59000, NULL, NOW(),
					'[{"url":"http://via.placeholder.com/300x300"}]',
					'[{"url":"https://igx.4sqi.net/img/general/width960/45442676_MxkidWnaJc31VIgZROYx-8AQ-sXL9DQCLrjf9A-kOVc.jpg"}]',
					'[{"url":"http://via.placeholder.com/300x300"}]');
INSERT INTO `omart_db`.`omart_poi_item`(`poi_id`, `id`, `name`, `description`, `unit_price`,
							`type`, `created_at`, `avatar_image`, `cover_image`, `featured_image`)
			VALUES (1, 2, 'Cơm tấm sườn nướng', NULL, 34000, NULL,  NOW(),
					'[{"url":"http://via.placeholder.com/300x300"}]',
					'[{"url":"https://igx.4sqi.net/img/general/width960/jIBNd1GMJmlLpdYyZ5u-W92Pg10QnuK84U1egcWSpMw.jpg"}]',
					'[{"url":"http://via.placeholder.com/300x300"}]');
INSERT INTO `omart_db`.`omart_poi_item`(`poi_id`, `id`, `name`, `description`, `unit_price`,
							`type`, `created_at`, `avatar_image`, `cover_image`, `featured_image`)
			VALUES (1, 3, 'Cơm tấm sườn bì chả', NULL, 5000, NULL,  NOW(),
					'[{"url":"http://via.placeholder.com/300x300"}]',
					'[{"url":"http://via.placeholder.com/1024x768"}]',
					'[{"url":"http://via.placeholder.com/300x300"}]');

INSERT INTO `omart_db`.`omart_poi_item`(`poi_id`, `id`, `name`, `description`, `unit_price`,
							`type`, `created_at`, `avatar_image`, `cover_image`, `featured_image`)
			VALUES (2, 4, 'Apple iPhone 7 Plus 256GB RED Edition', 'Điện thoại thông minh này kia kia nọ',
			          21390000, NULL,  NOW(),
						'[{"url":"https://store.storeimages.cdn-apple.com/4974/as-images.apple.com/is/image/AppleInc/aos/published/images/i/ph/iphone7/model/iphone7-model-select-201703?wid=424&hei=586&fmt=png-alpha&qlt=80&.v=1489097365439"}]',
						'[{"url":"https://store.storeimages.cdn-apple.com/4974/as-images.apple.com/is/image/AppleInc/aos/published/images/i/ph/iphone7plus/model/iphone7plus-model-select-201703_AV3?wid=1290&hei=532&fmt=jpeg&qlt=80&op_sharpen=0&resMode=bicub&op_usm=0.5,0.5,0,0&iccEmbed=0&layer=comp&.v=1488827423771"},{"url":"https://store.storeimages.cdn-apple.com/4974/as-images.apple.com/is/image/AppleInc/aos/published/images/i/ph/iphone7/gallery1/iphone7-gallery1-201703?wid=2136&hei=1286&fmt=jpeg&qlt=80&op_sharpen=0&resMode=bicub&op_usm=0.5,0.5,0,0&iccEmbed=0&layer=comp&.v=1489093012289"}]',
						'[{"url":"http://via.placeholder.com/300x300"}]');
INSERT INTO `omart_db`.`omart_poi_item`(`poi_id`, `id`, `name`, `description`, `unit_price`,
							`type`, `created_at`, `avatar_image`, `cover_image`, `featured_image`)
			VALUES (2, 5, 'Loa bluetooth Sony SRS-XB20', 'Loa bluetooth 3.0 siêu to',
			          1990000, NULL,  NOW(),
						'[{"url":"http://via.placeholder.com/300x300"}]',
						'[{"url":"https://sonyglobal.scene7.com/is/image/gwtprod/b7cd2d50bdda5299e664d678383ade7c?fmt=pjpeg&wid=220&bgcolor=FFFFFF&bgc=FFFFFF"}]',
						'[{"url":"http://via.placeholder.com/300x300"}]');
INSERT INTO `omart_db`.`omart_poi_item`(`poi_id`, `id`, `name`, `description`, `unit_price`,
							`type`, `created_at`, `avatar_image`, `cover_image`, `featured_image`)
			VALUES (2, 6, 'Pin dự phòng Anker 13000', 'Pin dung lượng cao, sạc đã đời',
			          900000, NULL,  NOW(),
						'[{"url":"http://via.placeholder.com/300x300"}]',
						'[{"url":"http://thanhphukien.com/public/media/images/Pin%20d%E1%BB%B1%20ph%C3%B2ng/Anker/13k/1.jpg"}]',
						'[{"url":"http://via.placeholder.com/300x300"}]');