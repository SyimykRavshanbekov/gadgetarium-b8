insert into users_info(id,email,password,role)
values (1,'azimbek1@gmail.com','$2a$12$qT1185usfTsvB6R6WZYl4O2JiUtg6UxyfPxcqnkruhOqVEf/iPmlK','ADMIN'),        -- password: azimbek11
       (2,'altyn1@gmail.com','$2a$12$fT3foWsvu6YXkpFjWWu3z.zDNQMIycDDTwr7CMWa9KMb1WGEpFdeO','USER');           -- password: altyn11

insert into users(id, address, first_name, image, last_name, phone_number, user_info_id)
VALUES (1,'Гражданская 119','Азимбек','https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8M3x8dXNlcnxlbnwwfHwwfHw%3D&w=1000&q=80','Абдивалиев','+996222899897',1),
       (2,'Чуй 119','Алтын','https://thumbs.dreamstime.com/b/portrait-chubby-young-man-against-gray-background-portrait-chubby-young-man-against-gray-background-154513213.jpg','Шакиров','+996770098908',2);

insert into customers (id,address,email,first_name,last_name,phone_number)
values (1,'Московский Фучика 268/3','azimbek279@gmail.com','Азимбек','Абдивалиев','+996777877887'),
       (2,'Байтик Батыр 229','beka279@gmail.com','Бека','Ташкенбаев','+996777877887'),
       (3,'Asia 4/3','marlen@gmail.com','Марлен','Марленов','+996777877887'),
       (4,'Киевский 222','kutman@gmail.com','Кутман','Кутманов','+996777877887'),
       (5,'Фучика 543','chris@gmail.com','Крис','Хрисов','+996777877887');

insert into brands(id,logo,name)
values (1,'https://alchemyimmersive.com/wp-content/uploads/sites/4/2020/04/apple-logo-transparent.png','Apple'),
       (2,'https://1000logos.net/wp-content/uploads/2017/06/Samsung-Logo-2.png','Samsung'),
       (3,'https://static.vecteezy.com/system/resources/previews/016/680/500/original/acer-logo-editorial-logo-free-vector.jpg','Acer'),
       (4,'https://logos-world.net/wp-content/uploads/2020/07/Asus-Logo.png','Asus'),
       (5,'https://1000logos.net/wp-content/uploads/2021/08/Xiaomi-logo.png','Xiaomi');

insert into categories(id,name)
values (1,'Смартфон'),
       (2,'Планшет'),
       (3,'Ноутбук'),
       (4,'Смарт Часы');

insert into sub_categories(id,name,category_id)
values (1,'Apple',1),
       (2,'Apple',2),
       (3,'Asus',3),
       (4,'Apple',4);

insert into discounts(id,date_of_finish,date_of_start,percent)
values (1,'2023-09-01','2023-01-01',55),
        (2,'2023-08-01','2023-01-01',20),
        (3,'2023-07-01','2023-01-01',25),
        (4,'2023-06-01','2023-01-01',10),
        (5,'2023-05-01','2023-01-01',5);

insert into malling_list (id,date_of_finish,date_of_start,description,image,name)
values (1,'2023-02-01','2023-01-01','Скидка только для вас!','link','Скидка на телефоны!'),
       (2,'2023-03-01','2023-01-01','Скидка только для вас!','link','Скидка на Планшет!'),
       (3,'2023-04-01','2023-01-01','Скидка только для вас!','link','Скидка на ноутбук!'),
       (4,'2023-05-01','2023-01-01','Скидка только для вас!','link','Скидка на умные часы!');

insert into products (id,pdf,created_at,
                      date_of_issue,description,
                      guarantee,name,
                      rating,video,brand_id,discount_id,
                      sub_category_id)
values (1,'pdf link','2023-04-01','2020-01-01','Good product',12,'IPHONE 12',5,'https://youtu.be/Mla7Y-iQSq0',1,1,1),
       (2,'pdf link','2023-02-15','2019-01-01','Good product',12,'Vivobook S 14 Flip OLED',4.5,'https://youtu.be/MZavnAdb-_s',4,2,3),
       (3,'pdf link','2023-01-29','2021-01-01','Good product',12,'Smart Watch',3,'https://youtu.be/mLwVeGK0ViA',1,1,1),
       (4,'pdf link','2022-12-11','2022-01-01','Good product',12,'IPHONE 14 PRO MAX',5,'https://youtu.be/Mla7Y-iQSq0',1,1,1),
       (5,'pdf link','2023-03-19','2018-01-01','Good product',12,'Mac Book',1,'https://youtu.be/Hw7iB-zkYrk',1,1,1);

insert into sub_products (id,colour,price,quantity,item_number,product_id)
values (1,'Blue',120000,1,'123',1),
       (2,'Red',76000,1,'321',2),
       (3,'Black',76000,1,'321',2),
       (4,'Pink',76000,1,'321',2),
       (5,'Black',111000,1,'234',3),
       (6,'Green',111000,1,'234',3),
       (7,'Space grey',111000,1,'234',3),
       (8,'Purple',55000,1,'432',4),
       (9,'Blue',55000,1,'432',4),
       (10,'White',3236000,1,'345',5);

insert into orders (id,date,delivery_type,order_number,payment_type,quantity,status,total_price,customer_id,user_id)
values (1,'2023-01-01',true,'1','BY_CARD_ONLINE',1,'PENDING',55000,1,1),
       (2,'2023-01-01',true,'1','BY_CARD_OFFLINE',1,'DELIVERED',55000,1,1),
       (3,'2023-01-01',true,'1','BY_CARD_ONLINE',1,'CANCEL',55000,1,1),
       (4,'2023-01-01',true,'1','BY_CASH',1,'COURIER_ON_THE_WAY',55000,1,1),
       (5,'2023-01-01',true,'1','BY_CARD_OFFLINE',1,'DELIVERED',55000,1,1);

insert into malling_list_subscribers (id,user_email)
values (1,'azimbek1@gmail.com'),
       (2,'beka279@gmail.com'),
       (3,'marlen@gmail.com'),
       (4,'kutman@gmail.com'),
       (5,'chris@gmail.com');

insert into orders_sub_products (order_id,sub_products_id)
values (1,1),
       (2,2),
       (3,3),
       (4,4),
       (5,5);

insert into reviews (id,answer,commentary,grade,product_id,user_id)
values (1,'answer','commentary',5,1,1),
       (2,'answer','commentary',4,2,1),
       (3,'answer','commentary',1,3,1),
       (4,'answer','commentary',3,4,1),
       (5,'answer','commentary',4,5,1);

insert into review_images (review_id,images)
values (1,'https://yt3.googleusercontent.com/ytc/AGIKgqNoNRD8Y7-ydomwccOXCRsrtM3SVG1veHCKxN5IOg=s900-c-k-c0x00ffffff-no-rj'),
       (2,'https://bobbystore.kg/wa-data/public/shop/img/bez_nazvaniya_10.png'),
       (3,'https://images.acer.com/is/image/acer/Spin-5-SP513-55N-FP-Bakclit-Steel-Gray-01a-1?$Product-Cards-XL$'),
       (4,'https://gudini.kg/image/cache/catalog/Apple/Watch%20Series%208/1115-800x800.jpg'),
       (5,'https://m.media-amazon.com/images/I/71TPda7cwUL._SL1500_.jpg');

insert into sub_product_characteristics(sub_product_id,characteristics,characteristics_key)
values (1,'256GB','память'),
       (2,'512GB','память'),
       (3,'256GB','память'),
       (4,'1T','память'),
       (5,'128GB','память'),
       (1,'1024x600','разрешение экрана'),
       (2,'2340x1200','разрешение экрана'),
       (3,'2340x1200','разрешение экрана'),
       (4,'2340x1200','разрешение экрана'),
       (5,'2340x1200','разрешение экрана'),
       (1,'13.3','диогональ экрана'),
       (2,'13.3','диогональ экрана'),
       (3,'13.3','диогональ экрана'),
       (4,'13.3','диогональ экрана'),
       (5,'13.3','диогональ экрана'),
       (1,'16','RAM'),
       (2,'16','RAM'),
       (3,'16','RAM'),
       (4,'16','RAM'),
       (5,'16','RAM'),
       (1,'7200 mA/h','емкость аккумулятора(mA/h)'),
       (2,'7200 mA/h','емкость аккумулятора(mA/h)'),
       (3,'7200 mA/h','емкость аккумулятора(mA/h)'),
       (4,'7200 mA/h','емкость аккумулятора(mA/h)'),
       (5,'7200 mA/h','емкость аккумулятора(mA/h)');

insert into sub_product_images (sub_product_id,images)
values (1,'https://login.kg/image/cache/catalog/new/Phones/Apple/iPhone%2014/Pro-Pro%20Max/1-500x500.jpg'),
       (1,'https://login.kg/image/cache/catalog/new/Phones/Apple/iPhone%2014/Pro-Pro%20Max/1-500x500.jpg'),
       (1,'https://login.kg/image/cache/catalog/new/Phones/Apple/iPhone%2014/Pro-Pro%20Max/1-500x500.jpg'),
       (2,'https://www.litech.kg/storage/products/November2022/FCnalG4PX0ZYoq5EqmbH.jpg'),
       (2,'https://www.litech.kg/storage/products/November2022/FCnalG4PX0ZYoq5EqmbH.jpg'),
       (2,'https://www.litech.kg/storage/products/November2022/FCnalG4PX0ZYoq5EqmbH.jpg'),
       (3,'https://mstore.kg/wp-content/uploads/2022/02/часы.png'),
       (3,'https://mstore.kg/wp-content/uploads/2022/02/часы.png'),
       (3,'https://mstore.kg/wp-content/uploads/2022/02/часы.png'),
       (3,'https://mstore.kg/wp-content/uploads/2022/02/часы.png'),
       (4,'https://login.kg/image/cache/catalog/new/Phones/Apple/iPhone%2014/Pro-Pro%20Max/1-500x500.jpg'),
       (4,'https://login.kg/image/cache/catalog/new/Phones/Apple/iPhone%2014/Pro-Pro%20Max/1-500x500.jpg'),
       (4,'https://login.kg/image/cache/catalog/new/Phones/Apple/iPhone%2014/Pro-Pro%20Max/1-500x500.jpg'),
       (5,'https://m.media-amazon.com/images/I/71TPda7cwUL._SL1500_.jpg'),
       (5,'https://m.media-amazon.com/images/I/71TPda7cwUL._SL1500_.jpg'),
       (5,'https://m.media-amazon.com/images/I/71TPda7cwUL._SL1500_.jpg'),
       (5,'https://m.media-amazon.com/images/I/71TPda7cwUL._SL1500_.jpg');

insert into user_basket (user_id,basket,basket_key)
values (1,1,1),
       (1,2,2),
       (1,3,3),
       (1,4,4),
       (1,5,5);

insert into users_comparisons (user_id,comparisons_id)
values (1,1),
       (1,2),
       (1,3),
       (1,4),
       (1,5);

insert into users_favorites (user_id,favorites_id)
values (1,1),
       (1,2),
       (1,3),
       (1,4),
       (1,5);

insert into users_last_views (user_id,last_views_id)
values (1,1),
       (1,2),
       (1,3),
       (1,4),
       (1,5);