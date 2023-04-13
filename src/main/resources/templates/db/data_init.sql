insert into users_info(id,email,password,role)
values (1,'azimbek1@gmail.com','azimbek11','ADMIN');
values (2,'altyn1@gmail.com','altyn11','USER');

insert into users(id, address, first_name, image, last_name, phone_number, user_info_id)
VALUES (1,'Гражданская 119','Азимбек','link','Абдивалиев','+996 222 899 897',1);
VALUES (2,'Чуй 119','Алтын','link','Шакиров','+996 770 098 908',2);

insert into customers (id,address,email,first_name,last_name,phone_number)
values (1,'Московский Фучика 268/3','azimbek279@gmail.com','Азимбек','Абдивалиев','+996 777 87 78 87'),
       (2,'Байтик Батыр 229','beka279@gmail.com','Бека','Ташкенбаев','+996 777 87 78 87'),
       (3,'Asia 4/3','marlen@gmail.com','Марлен','Марленов','+996 777 87 78 87'),
       (4,'Киевский 222','kutman@gmail.com','Кутман','Кутманов','+996 777 87 78 87'),
       (5,'Фучика 543','chris@gmail.com','Крис','Хрисов','+996 777 87 78 87');

insert into brands(id,logo,name)
values (1,'link','Apple'),
       (2,'link','Samsung'),
       (3,'link','Acer'),
       (4,'link','Asus'),
       (5,'link','Xiaomi');

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
values (1,'2023-09-01','2023-01-01',90),
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
                      guarantee,item_number,name,
                      rating,video,brand_id,discount_id,
                      sub_category_id)
values (1,'pdf link','2023-04-01','2020-01-01','Good product',12,'1','IPHONE 12',5,'video link',1,1,1),
       (2,'pdf link','2023-04-01','2019-01-01','Good product',12,'1','Asus',5,'video link',4,2,3),
       (3,'pdf link','2023-04-01','2021-01-01','Good product',12,'1','Apple Watch',5,'video link',1,1,1),
       (4,'pdf link','2023-04-01','2022-01-01','Good product',12,'1','IPHONE 14 PRO MAX',5,'video link',1,1,1),
       (5,'pdf link','2023-04-01','2018-01-01','Good product',12,'1','Mac Book',5,'video link',1,1,1);

insert into sub_products (id,colour,price,quantity,product_id)
values (1,'Blue',55000,1,1),
       (2,'Red',55000,1,2),
       (3,'Black',55000,1,3),
       (4,'Purple',55000,1,4),
       (5,'White',55000,1,5);

insert into orders (id,date,delivery_type,order_number,payment_type,quantity,status,total_price,customer_id,user_id)
values (1,'2023-01-01',true,'1',1,1,1,55000,1,1),
       (2,'2023-01-01',true,'1',1,1,1,55000,1,1),
       (3,'2023-01-01',true,'1',1,1,1,55000,1,1),
       (4,'2023-01-01',true,'1',1,1,1,55000,1,1),
       (5,'2023-01-01',true,'1',1,1,1,55000,1,1);

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
       (2,'answer','commentary',5,2,1),
       (3,'answer','commentary',5,3,1),
       (4,'answer','commentary',5,4,1),
       (5,'answer','commentary',5,5,1);

insert into review_images (review_id,images)
values (1,'link'),
       (2,'link'),
       (3,'link'),
       (4,'link'),
       (5,'link');

insert into sub_product_characteristics(sub_product_id,characteristics,characteristics_key)
values (1,'characteristics',1),
       (2,'characteristics',1),
       (3,'characteristics',1),
       (4,'characteristics',1),
       (5,'characteristics',1);

insert into sub_product_images (sub_product_id,images)
values (1,'link'),
       (2,'link'),
       (3,'link'),
       (4,'link'),
       (5,'link');

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