insert into users_info(id,email,password,role)
values (1,'azimbek1@gmail.com','$2a$12$qT1185usfTsvB6R6WZYl4O2JiUtg6UxyfPxcqnkruhOqVEf/iPmlK','ADMIN'),        -- password: azimbek11
       (2,'altyn1@gmail.com','$2a$12$fT3foWsvu6YXkpFjWWu3z.zDNQMIycDDTwr7CMWa9KMb1WGEpFdeO','USER'),           -- password: altyn11
       (3, 'kurstanerkinbaev2@gmail.com','$2a$12$axxv6sMe.t/FKq3zPZIJX.tpSz06lfMnDs0syveIH3X4mEEHBHcB.','USER');        --password Kurstan22

insert into users(id, address, first_name, image, last_name, phone_number, user_info_id)
VALUES (1,'Гражданская 119','Азимбек','https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8M3x8dXNlcnxlbnwwfHwwfHw%3D&w=1000&q=80','Абдивалиев','+996222899897',1),
       (2,'Чуй 243','Алтын','https://gadgetariumbucket.s3.eu-central-1.amazonaws.com/1687720276534msg1245913207-27323.jpg','Шакиров','+996770098908',2),
       (3, 'Gorkiy 123', 'Kurstan', 'https://gadgetariumbucket.s3.eu-central-1.amazonaws.com/1687720530651msg1245913207-27324.jpg','Erkinbaev',' +996220222333', 3);

insert into banners(id, banner)
values (1, 'https://www.nylon.com.sg/wp-content/uploads/2018/10/apple-ipad-pro-macbook-air-banner.jpg'),
       (2, 'https://www.ixbt.com/img/n1/news/2021/4/1/ipad-pro-wallpaper_large-scaled_large.png'),
       (3, 'https://cdn.shopify.com/s/files/1/1364/2765/articles/apple_watch_series_1-4_apple_watch_accessory_features_and_differences_banner.png?v=1558601134'),
       (4, 'https://www.apple.com/in/iphone-14-pro/images/key-features/features/dynamic-island/dd_dynamic_island__b44bx9hr74ty_large.jpg');

insert into brands(id,logo,name)
values (1,'https://logos-world.net/wp-content/uploads/2020/04/Apple-Logo.png','Apple'),
       (2,'https://logos-world.net/wp-content/uploads/2020/04/Samsung-Logo.png','Samsung'),
       (3,'https://logos-world.net/wp-content/uploads/2020/11/Honor-Logo.png','Honor'),
       (4,'https://logos-world.net/wp-content/uploads/2020/07/Xiaomi-Logo.png','Xiaomi'),
       (5,'https://logos-world.net/wp-content/uploads/2020/04/Huawei-Logo.png','Huawei'),
       (6,'https://logos-world.net/wp-content/uploads/2022/11/Acer-Logo.png','Acer'),
       (7,'https://logos-world.net/wp-content/uploads/2020/07/Asus-Logo.png','Asus'),
       (8,'https://logos-world.net/wp-content/uploads/2020/08/Dell-Logo.png','Dell'),
       (9,'https://logos-world.net/wp-content/uploads/2022/07/Lenovo-Logo.png','Lenovo'),
       (10,'https://logos-world.net/wp-content/uploads/2020/11/HP-Logo.png','HP');

insert into categories(id,name)
values (1,'Смартфон'),
       (2,'Планшет'),
       (3,'Ноутбук'),
       (4,'Смарт-часы');

insert into sub_categories(id,name,category_id)
values (1,'Apple',1),
       (2,'Samsung',1),
       (3,'Honor',1),
       (4,'Xiaomi',1),
       (5,'Huawei',1),

       (6,'Samsung',2),
       (7,'Apple',2),
       (8,'Honor',2),
       (9,'Xiaomi',2),
       (10,'Huawei',2),
       (11,'Acer',2),
       (12,'Asus',2),
       (13,'Dell',2),
       (14,'Lenovo',2),
       (15,'HP',2),

       (16,'Samsung',3),
       (17,'Apple',3),
       (18,'Honor',3),
       (19,'Xiaomi',3),
       (20,'Huawei',3),
       (21,'Acer',3),
       (22,'Asus',3),
       (23,'Dell',3),
       (24,'Lenovo',3),
       (25,'HP',3),

       (26,'Apple Watch',4),
       (27,'Для взрослых',4),
       (28,'Для детей',4),
       (29,'Фитнес браслеты',4);

insert into discounts(id,date_of_finish,date_of_start,percent)
values (1,'2023-09-01','2023-03-01',15),
       (2,'2023-08-01','2023-04-01',5),
       (3,'2023-07-01','2023-02-01',25),
       (4,'2023-06-30','2023-05-01',70),
       (5,'2023-06-29','2023-01-01',5);

insert into malling_list (id,date_of_finish,date_of_start,description,image,name)
values (1,'2023-07-01','2023-01-01','Скидка только для вас!','link','Скидка на умные часы!');

insert into products (id,pdf,created_at,
                      date_of_issue,description,
                      guarantee,name,
                      rating,video,brand_id,
                      sub_category_id)
values (1,'https://psref.lenovo.com/syspool/Sys/PDF/Legion/Legion_Pro_7_16IRX8/Legion_Pro_7_16IRX8_Spec.pdf','2023-06-24','2023-01-01',
        'iPhone 14 Pro и Pro Max оснащены OLED-дисплеем Super Retina XDR с типичной максимальной яркостью 1000 нит. Тем не менее, он может достигать 1600 нит при просмотре HDR-видео и 2000 нит на улице. Дисплей имеет частоту обновления 120 Гц и использует технологию LTPO.',
        3,'iPhone 14 Pro Max',5,'https://youtu.be/H4SXsmXZlwk',1,1),
       (2,'https://psref.lenovo.com/syspool/Sys/PDF/Legion/Legion_Pro_7_16IRX8/Legion_Pro_7_16IRX8_Spec.pdf','2023-05-01','2023-01-01',
        'Samsung Galaxy S23 Ultra является хедлайнером серии S23. Спецификации первоклассные, включая 6,8-дюймовый динамический AMOLED-дисплей с частотой обновления 120 Гц, процессор Snapdragon 8 Gen 2, аккумулятор емкостью 5000 мАч, до 12 ГБ оперативной памяти и 1 ТБ памяти. В отделе камер представлена четырехкамерная установка с двумя телеобъективами.',
        12,'Galaxy S23 Ultra',5,'https://youtu.be/wmgj8izYOKw',2,2),

       (3,'https://psref.lenovo.com/syspool/Sys/PDF/Legion/Legion_Pro_7_16IRX8/Legion_Pro_7_16IRX8_Spec.pdf','2023-06-25','2022-10-20',
        'Благодаря ядрам с революционной производительностью и эффективностью процессоры Intel® Core™ 12-го поколения обеспечивают бесперебойную потоковую передачу, редактирование, игры и запись, предоставляя мощность там, где она больше всего нужна. Поддержка Intel® Thread Director и Intel® WiFi 6E обеспечивает бесперебойную работу.',
        8,'Legion Pro 7i Gen 8 Intel (16″) with RTX 4090',4.5,'https://youtu.be/c7xe-gDOmBU',9,24),

       (4,'https://psref.lenovo.com/syspool/Sys/PDF/Legion/Legion_Pro_7_16IRX8/Legion_Pro_7_16IRX8_Spec.pdf','2023-03-29','2020-09-15',
        'Apple представила iPad Air 4 в сентябре 2020 года как следующую эволюцию линейки непрофессиональных планшетов. Он оснащен 10,9-дюймовым безрамочным дисплеем, Touch ID на кнопке питания и мощным процессором A14 Bionic. Он также добавляет поддержку Magic Keyboard, Apple Pencil второго поколения и USB-C.',
        12,'iPad Air (10.9 Gen 4 2020)',3,'https://youtu.be/Kb_kIq5tMGQ',1,7),
       (5,'https://psref.lenovo.com/syspool/Sys/PDF/Legion/Legion_Pro_7_16IRX8/Legion_Pro_7_16IRX8_Spec.pdf','2022-12-11','2022-01-01',
        'Планшет Mi Pad 5 получил большой экран 2,5К на 11 дюймов, который обеспечивает эффективную защиту глаз от синего света. За качественный звук отвечают 4 динамика с Dolby Atmos. Разрешение датчика основной камеры составляет 13 Мп, а фронтальной – 8 Мп. Поддерживается быстрая зарядка 33 Вт.',
        4,'MI PAD 5',3.2,'https://youtu.be/EuZT_uGLKUw',4,9),

       (6,'https://psref.lenovo.com/syspool/Sys/PDF/Legion/Legion_Pro_7_16IRX8/Legion_Pro_7_16IRX8_Spec.pdf','2023-06-23','2023-03-13',
        'Watch Ultra Оптический датчик пульса, датчик ЭКГ, пульсоксиметр, акселерометр, гироскоп ,датчикпадения, датчикосвещенности, альтиметр. Часы с возможностью распознования падения и аварии, с функцией отследивания сердечного ритма',
        2,'Watch Ultra',4.9,'https://youtu.be/E4qTBQQfnjM',1,26);

insert into sub_products (id,colour,price,quantity,item_number,product_id,discount_id)
values (1,'purple',134990,6,'12345',1,2),
       (2,'silver',140900,10,'21345',1,null),
       (3,'green',119900,13,'31245',2,3),
       (4,'beige',119900,19,'41235',2,null),

       (5,'gray',274800.99,20,'51234',3,4),

       (6,'silver',63990.99,17,'54321',4,null),
       (7,'gold',69800.99,8,'45321',4,4),

       (8,'white',31980.90,40,'35421',5,5),

       (9,'starlight',73690.90,10,'25431',6,1),
       (10,'green',73690.90,9,'15432',6,null),
       (11,'orange',73690.90,5,'45123',6,3);

insert into sub_product_characteristics(sub_product_id,characteristics,characteristics_key)
values (1,'256GB','память'),
       (1,'8','RAM'),
       (1,'2','Кол-во SIM-карт'),

       (2,'512GB','память'),
       (2,'16','RAM'),
       (2,'2','Кол-во SIM-карт'),

       (3,'1T','память'),
       (3,'16','RAM'),
       (3,'2','Кол-во SIM-карт'),

       (4,'512GB','память'),
       (4,'8','RAM'),
       (4,'2','Кол-во SIM-карт'),

       (5,'Intel Core i9-13900HX','Процессор'),
       (5,'2560x1600','Разрешение экрана'),
       (5,'2T','память'),
       (5,'16','Видеопамять'),
       (5,'16','Размер экрана(дюйм)'),
       (5,'16','RAM'),

       (6,'2360x1640','Разрешение экрана'),
       (6,'10.9','Размер экрана(дюйм)'),
       (6,'64GB','память'),
       (6,'8','RAM'),
       (6,'2400 мА/ч','Емкость аккумулятора'),

       (7,'2360x1640','Разрешение экрана'),
       (7,'10.9','Размер экрана(дюйм)'),
       (7,'64GB','память'),
       (7,'8','RAM'),
       (7,'6540 мА/ч','Емкость аккумулятора'),

       (8,'2560x1600 ','Разрешение экрана'),
       (8,'11','Размер экрана(дюйм)'),
       (8,'156GB','память'),
       (8,'6','RAM'),
       (8,'8720 мА/ч','Емкость аккумулятора'),

       (9,'32GB','память'),
       (9,'Альпийская петля','Материал браслета/ремешка'),
       (9,'Титановый','Материал корпуса'),
       (9,'1.73','Диагональ дисплея(дюйм)'),
       (9,'Унисекс','Пол'),
       (9,'Да','Водонепроницаемые'),
       (9,'Bluetooth, GPS','Беспроводные интерфейсы'),
       (9,'Овальная','Форма корпуса'),

       (10,'32GB','память'),
       (10,'Альпийская петля','Материал браслета/ремешка'),
       (10,'Титановый','Материал корпуса'),
       (10,'1.73','Диагональ дисплея(дюйм)'),
       (10,'Унисекс','Пол'),
       (10,'Да','Водонепроницаемые'),
       (10,'Bluetooth, GPS','Беспроводные интерфейсы'),
       (10,'Овальная','Форма корпуса'),

       (11,'32GB','память'),
       (11,'Альпийская петля','Материал браслета/ремешка'),
       (11,'Титановый','Материал корпуса'),
       (11,'1.73','Диагональ дисплея(дюйм)'),
       (11,'Унисекс','Пол'),
       (11,'Да','Водонепроницаемые'),
       (11,'Bluetooth, GPS','Беспроводные интерфейсы'),
       (11,'Овальная','Форма корпуса');


insert into sub_product_images (sub_product_id,images)
values (1,'https://asiastore.kg/image/cachewebp/catalog/iphone/iphone14/iphone14promax/deeppurple/wwen_iphone14promax_q422_deep-purple_pdp-images_position-1a-670x540.webp'),
       (1,'https://asiastore.kg/image/cachewebp/catalog/iphone/iphone14/iphone14promax/deeppurple/wwen_iphone14promax_q422_deep-purple_pdp-images_position-2-670x540.webp'),
       (1,'https://asiastore.kg/image/cachewebp/catalog/iphone/iphone14/iphone14promax/deeppurple/wwen_iphone14promax_q422_deep-purple_pdp-images_position-3-670x540.webp'),
       (1,'https://asiastore.kg/image/cachewebp/catalog/iphone/iphone14/iphone14promax/deeppurple/wwen_iphone14promax_q422_deep-purple_pdp-images_position-4-670x540.webp'),
       (2,'https://asiastore.kg/image/cachewebp/catalog/iphone/iphone14/iphone14promax/silver/wwen_iphone14promax_q422_silver_pdp-images_position-1a-670x540.webp'),
       (2,'https://asiastore.kg/image/cachewebp/catalog/iphone/iphone14/iphone14promax/silver/wwen_iphone14promax_q422_silver_pdp-images_position-2-670x540.webp'),
       (2,'https://asiastore.kg/image/cachewebp/catalog/iphone/iphone14/iphone14promax/silver/wwen_iphone14promax_q422_silver_pdp-images_position-3-670x540.webp'),
       (2,'https://asiastore.kg/image/cachewebp/catalog/iphone/iphone14/iphone14promax/silver/wwen_iphone14promax_q422_silver_pdp-images_position-4-670x540.webp'),
       (3,'https://www.myphone.kg/files/media/18/18971.webp'),
       (3,'https://www.myphone.kg/files/media/18/18973.webp'),
       (3,'https://www.myphone.kg/files/media/18/18974.webp'),
       (3,'https://www.myphone.kg/files/media/18/18972.webp'),
       (4,'https://www.myphone.kg/files/media/18/18976.webp'),
       (4,'https://www.myphone.kg/files/media/18/18977.webp'),
       (4,'https://www.myphone.kg/files/media/18/18975.webp'),
       (4,'https://www.myphone.kg/files/media/18/18978.webp'),
       (5,'https://p2-ofp.static.pub/fes/cms/2022/12/15/iiv0yj4xis18fq2p5zgbcubx18lggs470202.png'),
       (5,'https://p3-ofp.static.pub/fes/cms/2022/12/15/jetwzpg7d98zcjvjf0jekl1cd16qe7091589.png'),
       (5,'https://p4-ofp.static.pub/fes/cms/2022/12/15/dtxip3mv36eu540ay7n0uqx96bunsc204487.png'),
       (5,'https://p4-ofp.static.pub/fes/cms/2022/12/15/uef7zf0vtjbk6m1hqzcxvskddhvp09851403.png'),
       (5,'https://p3-ofp.static.pub/fes/cms/2022/12/15/eb45555m5m75bh0sn38ee31e0vpfk9794703.png'),
       (5,'https://p4-ofp.static.pub/fes/cms/2022/12/15/t5w7z02bmefplid4xgja3kl5uc43gw827995.png'),
       (6,'https://asiastore.kg/image/cachewebp/catalog/ipad/ipadair4/silver/71qla-tib%2Bl._ac_sl1500_-670x540.webp'),
       (6,'https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/ipadair-digitalmat-gallery-4-202203?wid=364&hei=333&fmt=jpeg&qlt=95&.v=1644962768511'),
       (6,'https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/ipadair-digitalmat-gallery-5-202203?wid=364&hei=333&fmt=png-alpha&.v=1644962768260'),
       (7,'https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/refurb-ipad-air-wifi-gold-2021?wid=572&hei=572&fmt=jpeg&qlt=95&.v=1644268571040'),
       (7,'https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/ipadair-digitalmat-gallery-3-202203?wid=364&hei=333&fmt=jpeg&qlt=95&.v=1644962768498'),
       (8,'https://cdn.ksyru0-fusion.fds.api.mi-img.com/b2c-mishop-pms-ru/pms_1641814452.62287137.png'),
       (8,'https://i01.appmifile.com/webfile/globalimg/products/pc/xiaomi-pad-5/sepcs01.png'),
       (9,'https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/MQE53ref_VW_34FR+watch-49-titanium-ultra_VW_34FR_WF_CO+watch-face-49-alpine-ultra_VW_34FR_WF_CO_GEO_GB?wid=700&hei=700&trim=1%2C0&fmt=p-jpg&qlt=95&.v=1683224241054'),
       (9,'https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/MQE53ref_VW_PF+watch-49-titanium-ultra_VW_PF_WF_CO+watch-face-49-alpine-ultra_VW_PF_WF_CO_GEO_GB?wid=700&hei=700&trim=1%2C0&fmt=p-jpg&qlt=95&.v=1683224241054'),
       (9,'https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/MQE53ref?wid=700&hei=700&trim=1%2C0&fmt=p-jpg&qlt=95&.v=1660715723119'),
       (10,'https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/MQE23ref_VW_34FR+watch-49-titanium-ultra_VW_34FR_WF_CO+watch-face-49-alpine-ultra_VW_34FR_WF_CO_GEO_GB?wid=700&hei=700&trim=1%2C0&fmt=p-jpg&qlt=95&.v=1683224241054'),
       (10,'https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/MQE23ref_VW_PF+watch-49-titanium-ultra_VW_PF_WF_CO+watch-face-49-alpine-ultra_VW_PF_WF_CO_GEO_GB?wid=700&hei=700&trim=1%2C0&fmt=p-jpg&qlt=95&.v=1683224241054'),
       (10,'https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/MQE23ref?wid=700&hei=700&trim=1%2C0&fmt=p-jpg&qlt=95&.v=1660715727375'),
       (11,'https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/MQDY3ref_VW_34FR+watch-49-titanium-ultra_VW_34FR_WF_CO+watch-face-49-alpine-ultra_VW_34FR_WF_CO_GEO_GB?wid=700&hei=700&trim=1%2C0&fmt=p-jpg&qlt=95&.v=1683224241054'),
       (11,'https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/MQDY3ref_VW_PF+watch-49-titanium-ultra_VW_PF_WF_CO+watch-face-49-alpine-ultra_VW_PF_WF_CO_GEO_GB?wid=700&hei=700&trim=1%2C0&fmt=p-jpg&qlt=95&.v=1683224241054'),
       (11,'https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/MQDY3ref?wid=700&hei=700&trim=1%2C0&fmt=p-jpg&qlt=95&.v=1660715729849');

insert into customers (id,address,email,first_name,last_name,phone_number)
values (1,'Gorkiy 268/3','erkinbaev@gmail.com','Kurstan','Erkinbev','+996220223344'),
       (2,'Байтик Батыр 229','altynbek@gmail.com','Altynbek','Shakirov','+996777877887'),
       (3,'Фучика 543','azimbek@gmail.com','Azimbek','Abdivaliev','+996777877887');

insert into orders (id,date,delivery_type,order_number,payment_type,quantity,status,total_price,customer_id,user_id)
values (1,'2023-04-30',true,'12345','BY_CARD_ONLINE',3,'PENDING',544780.99,1,2),
       (2,'2023-03-13',false,'21345','BY_CASH',5,'PENDING',359700,2,2),
       (3,'2023-05-01',false,'31245','BY_CARD_OFFLINE',2,'PENDING',63960,1,3),
       (4,'2023-06-24',true,'41235','BY_CASH',2,'DELIVERED',147380,1,2),
       (5,'2023-04-01',true,'51234','BY_CARD_ONLINE',1,'DELIVERED',63990.99,3,3),
       (6,'2023-03-28',false,'54321','BY_CARD_ONLINE',1,'CANCEL',69800.99,1,2),
       (7,'2023-04-30',true,'45321','BY_CASH',1,'COURIER_ON_THE_WAY',119900,2,2),
       (8,'2023-06-25',true,'34521','BY_CASH',1,'COURIER_ON_THE_WAY',140900,2,2),
       (9,'2023-05-25',true,'23451','BY_CARD_ONLINE',1,'READY_FOR_DELIVERY',73690.90,3,3),
       (10,'2022-12-23',false,'15432','BY_CARD_ONLINE',3,'RECEIVED',221072.7,1,3);

insert into malling_list_subscribers (id,user_email)
values (1,'kurstanerkinbaev2@gmail.com'),
       (2,'feruzacazacova@gmail.com');

insert into orders_sub_products (order_id,sub_products_id)
values (1,1),
       (1,5),
       (2,3),
       (3,8),
       (4,10),
       (5,6),
       (6,7),
       (7,4),
       (8,2),
       (9,9),
       (10,11);

insert into reviews (id,answer,commentary,grade,product_id,user_id)
values (1,'Добрый день! Благодарим Вас....',
        'Просто потрясающий дисплей! Я восхищен качеством изображения на OLED-дисплее Super Retina XDR. Яркость на улице действительно впечатляющая, и HDR-видео просто оживает на экране. Частота обновления 120 Гц делает прокрутку и анимацию очень плавными. Очень доволен технологией LTPO.',
        5,1,2),
       (2,null,
        'HDR-видео выглядит потрясающе. Единственное, что хотелось бы видеть улучшенным, это управление частотой обновления экрана в зависимости от контента, чтобы сохранить заряд батареи.',
        4,1,3),
       (3,null,
        'iPhone 14 Pro и Pro Max - идеальный выбор для геймеров. Очень плавный дисплей с частотой обновления 120 Гц делает игровой процесс более реалистичным и погружающим.',
        5,1,2),
       (4,null,
        'Частота обновления 120 Гц делает все очень плавным. Однако при длительном использовании с высокой частотой обновления замечается некоторое снижение времени работы от батареи.',
        3,1,3),

       (5,'Спасибо за ваш отзыв!',
        'Батарея емкостью 5000 мАч обеспечивает долгое время работы без подзарядки. Камеры тоже впечатляют, особенно два телеобъектива, которые позволяют делать прекрасные снимки в разных условиях.',
        4,2,3),
       (6,null,
        'Samsung Galaxy S23 Ultra - истинное воплощение мощи и производительности. Огромный динамический AMOLED-дисплей обеспечивает яркие и четкие изображения. Процессор Snapdragon 8 Gen 2 и 12 ГБ оперативной памяти делают работу с приложениями очень быстрой и плавной.',
        5,2,2),
       (7,null,
        'Четырехкамерная установка с двумя телеобъективами дает огромные возможности для творчества. Фотографии с высокой детализацией и отличной цветопередачей.',
        4,2,3),

       (8,null,
        'Процессоры Intel® Core™ 12-го поколения - это настоящий прорыв в производительности! Я впечатлен их мощностью и эффективностью.',
        4,3,3),
       (9,'Спасибо за вашу поддержку!',
        'Процессоры Intel® Core™ 12-го поколения предоставляют высокую мощность и эффективность для различных задач. Они без проблем справляются с потоковым воспроизведением, редактированием и играми.',
        5,3,2),
       (10,null,
        'Поддержка Intel® WiFi 6E также обеспечивает быструю и стабильную передачу данных. Рекомендую всем творческим профессионалам!',
        4,3,3),

       (11,'Спосибо за коментарии и оценку!!',
        'Apple представила iPad Air 4 в сентябре 2020 года как следующую эволюцию линейки непрофессиональных планшетов. Он оснащен 10,9-дюймовым безрамочным дисплеем, Touch ID на кнопке питания и мощным процессором A14 Bionic. Он также добавляет поддержку Magic Keyboard, Apple Pencil второго поколения и USB-C.',
        4,4,2),
       (12,null,
        'Процессор A14 Bionic работает на высоком уровне, обеспечивая быструю и плавную работу приложений.',
        5,4,3),
       (13,null,
        'Я также в восторге от поддержки Magic Keyboard и Apple Pencil второго поколения, что делает этот планшет идеальным инструментом для творчества и профессиональных задач. Рекомендую его всем!',
        4,4,2),

       (14,'Спосибо за коментарии',
        'Mi Pad 5 - впечатляющий планшет с большим и качественным экраном. Его 2,5К разрешение на 11 дюйма обеспечивает яркие и четкие изображения, а защита глаз от синего света делает использование планшета комфортным на протяжении длительного времени',
        4,5,2),
       (15,null,
        'Быстрая зарядка 33 Вт обеспечивает быстрое восстановление заряда планшета, что очень удобно.',
        5,5,3),
       (16,null,
        'Камеры на планшете удовлетворительные для обычного использования. Особо хочется отметить быструю зарядку.',
        4,5,2),

       (17,'Спосибо за коментарии',
        'Watch Ultra - это удивительные часы для здорового образа жизни! Они оснащены всеми необходимыми датчиками, включая оптический датчик пульса, датчик ЭКГ, пульсоксиметр, акселерометр, гироскоп, датчик падения, датчик освещенности и альтиметр',
        4,6,2),
       (18,'Thank you',
        ' Функция распознавания падения и аварии добавляет безопасности в повседневную жизнь, а отслеживание сердечного ритма помогает следить за своим здоровьем. ',
        5,6,3),
       (19,null,
        'Однако, мне бы хотелось видеть более детальный и точный анализ данных, предоставляемых этими часами.',
        4,6,2);


insert into review_images (review_id,images)
values (1,'https://www.digitaltrends.com/wp-content/uploads/2022/09/iPhone-14-Pro-Back-Purple-Hand.jpg?p=1'),
       (6,'https://www.ephotozine.com/articles/samsung-galaxy-s23--samsung-galaxy-s23-----samsung-galaxy-s23-ultra-hands-on-review-36366/images/highres-P1010028_1675257673.jpg'),
       (6,'https://www.ephotozine.com/resize/articles/36366/1000-P1010034_1675257701.jpg?RTUdGk5cXyJFCgsJVANtdxU+cVRdHxFYFw1Gewk0T1JYFEtzen5YdgthHHsyBFtG'),
       (8,'https://external-preview.redd.it/first-gaming-computer-since-2010-or-so-rtx-4090-lenovo-v0-vrsODeExj9K6D-wbKIUff59-UhAznxmTREh-UchUex8.jpg?width=1080&crop=smart&auto=webp&v=enabled&s=d5a8c981516a578967953382920beca33a2cabd0'),
       (12,'https://www.lifewire.com/thmb/CU7mMLG11EEx2VLHdSLtvBCqQck=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/Apple-iPad-Air-4th-Generation-2-fb9a5ccf82f144db8d71b352e2ce9c3b.jpg'),
       (12,'https://www.lifewire.com/thmb/G-vzjBAda3ougoZo0aJIbIzCmrQ=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/_hero_horiz_Apple-iPad-Air-4th-Generation-1-d02144f8233447e28f96969f0ac1459e.jpg'),
       (14,'https://www.techreviewer.de/wp-content/uploads/2021/09/xiaomi-pad-5-tablet-home_1.jpg'),
       (14,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQkmb1HdIRml_ofTRRFg8grDbKn4H5RuIWhYkS0JI-EusKPkqbdPNVqmilo_9KEB7MSUGY&usqp=CAU'),
       (17,'https://blog.loozap.com/wp-content/uploads/2023/02/lapple-watch-aux-lacs-robert-source-maxime-g.jpeg'),
       (17,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTuD5WHb6rV80m1SbFbW1gZFAX4RMQ905VQJr-OlgFG5jotF6iJbCmt6IbFLzxUDj9UZy0&usqp=CAU');

insert into user_basket (user_id,basket,basket_key)
values (2,1,1),
       (2,2,2),
       (3,3,3),
       (3,4,4),
       (3,5,5);

insert into users_comparisons (user_id,comparisons_id)
values (2,1),
       (3,4),
       (2,7),
       (3,8),
       (2,11);

insert into users_favorites (user_id,favorites_id)
values (3,10),
       (3,5),
       (3,6),
       (2,8),
       (2,2);

insert into users_last_views (user_id,last_views_id)
values (2,1),
       (2,5),
       (2,8),
       (2,11),
       (3,5);