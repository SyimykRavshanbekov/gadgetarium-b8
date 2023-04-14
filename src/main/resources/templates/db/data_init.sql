insert into users_info(id,email,password,role)
values (1,'azimbek1@gmail.com','azimbek11','ADMIN');
values (2,'altyn1@gmail.com','altyn11','USER');

insert into users(id, address, first_name, image, last_name, phone_number, user_info_id)
VALUES (1,'Гражданская 119','Азимбек','https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.gq.com%2Fstory%2Fgoogle-year-in-review-how-to-use-beard-balm&psig=AOvVaw2wR_05BCDqn8l80ZN_ADDj&ust=1681554690507000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCKiW08GVqf4CFQAAAAAdAAAAABAE','Абдивалиев','+996 222 899 897',1);
VALUES (2,'Чуй 119','Алтын','https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.vectorstock.com%2Froyalty-free-vector%2Fflat-business-man-user-profile-avatar-icon-vector-4333097&psig=AOvVaw30QAJsJjj4dRNu9W_t2loH&ust=1681554360765000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCID--aSUqf4CFQAAAAAdAAAAABAI','Шакиров','+996 770 098 908',2);

insert into customers (id,address,email,first_name,last_name,phone_number)
values (1,'Московский Фучика 268/3','azimbek279@gmail.com','Азимбек','Абдивалиев','+996 777 87 78 87'),
       (2,'Байтик Батыр 229','beka279@gmail.com','Бека','Ташкенбаев','+996 777 87 78 87'),
       (3,'Asia 4/3','marlen@gmail.com','Марлен','Марленов','+996 777 87 78 87'),
       (4,'Киевский 222','kutman@gmail.com','Кутман','Кутманов','+996 777 87 78 87'),
       (5,'Фучика 543','chris@gmail.com','Крис','Хрисов','+996 777 87 78 87');

insert into brands(id,logo,name)
values (1,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fsvetofor.info%2Fapple%2F&psig=AOvVaw2U6CFQPsaNh-QCWQa8hzCJ&ust=1681554713604000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCMjv8syVqf4CFQAAAAAdAAAAABAe','Apple'),
       (2,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fcommons.wikimedia.org%2Fwiki%2FFile%3ASamsung_Logo.svg&psig=AOvVaw2_36sInRaxJTrkJrXGAqZ8&ust=1681554751523000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCJi6096Vqf4CFQAAAAAdAAAAABAJ','Samsung'),
       (3,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.vecteezy.com%2Fvector-art%2F16680500-acer-logo-vector-editorial-logo&psig=AOvVaw2QQjwW6q-dSi8IQkbrM0e1&ust=1681554779062000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCPjU7OuVqf4CFQAAAAAdAAAAABAE','Acer'),
       (4,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.asus.com%2F&psig=AOvVaw14cPajGIGCfvnn3qnqom1E&ust=1681554791063000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCLjewvGVqf4CFQAAAAAdAAAAABAE','Asus'),
       (5,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fcommons.wikimedia.org%2Fwiki%2FFile%3AXiaomi_logo.svg&psig=AOvVaw08265G1pdaasKAK5RRk1kR&ust=1681554813311000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCOjxv_yVqf4CFQAAAAAdAAAAABAJ','Xiaomi');

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
values (1,'pdf link','2023-04-01','2020-01-01','Good product',12,'1','IPHONE 12',5,'https://youtu.be/Mla7Y-iQSq0',1,1,1),
       (2,'pdf link','2023-04-01','2019-01-01','Good product',12,'1','Asus',5,'https://youtu.be/MZavnAdb-_s',4,2,3),
       (3,'pdf link','2023-04-01','2021-01-01','Good product',12,'1','Apple Watch',5,'https://youtu.be/mLwVeGK0ViA',1,1,1),
       (4,'pdf link','2023-04-01','2022-01-01','Good product',12,'1','IPHONE 14 PRO MAX',5,'https://youtu.be/Mla7Y-iQSq0',1,1,1),
       (5,'pdf link','2023-04-01','2018-01-01','Good product',12,'1','Mac Book',5,'https://youtu.be/Hw7iB-zkYrk',1,1,1);

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
values (1,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.ixbt.com%2Fnews%2F2021%2F09%2F15%2Fv-kakih-regionah-mira-samye-dorogie-i-deshjovye-iphone-13.html&psig=AOvVaw2ScW-tvbzdDxOwGC0hw0Qg&ust=1681554986902000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCOCi-s6Wqf4CFQAAAAAdAAAAABAI'),
       (2,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.tomsguide.com%2Freviews%2Fapple-watch-7&psig=AOvVaw1UgdnZs99i7zSvwIwjdY51&ust=1681555078484000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCLi_0vqWqf4CFQAAAAAdAAAAABAE'),
       (3,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.ixbt.com%2Fnews%2F2020%2F10%2F28%2Fasus-vga-expertbook-p2451.html&psig=AOvVaw0LDmeY1sQ0KKuAyoA72gj4&ust=1681555111679000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCKD2vIqXqf4CFQAAAAAdAAAAABAE'),
       (4,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.digitaltrends.com%2Fcomputing%2Fapple-macbook-air-m2-review%2F&psig=AOvVaw3IoccmJpr9iiLze6QnAnle&ust=1681555133848000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCMCGoZWXqf4CFQAAAAAdAAAAABAE'),
       (5,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fsoftech.kg%2Fnoutbuk-acer-extensa-15-6-i5-1035g1-10th-gen-intel-uhd-graphics-8512gb-ssd&psig=AOvVaw32Hz4xHvtqJtmXids3fGEZ&ust=1681555148467000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCKi-_JuXqf4CFQAAAAAdAAAAABAE');

insert into sub_product_characteristics(sub_product_id,characteristics,characteristics_key)
values (1,'characteristics',1),
       (2,'characteristics',1),
       (3,'characteristics',1),
       (4,'characteristics',1),
       (5,'characteristics',1);

insert into sub_product_images (sub_product_id,images)
values (1,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.grover.com%2Fus-en%2Fproducts%2Fapple-smartphone-iphone-12-pro-max-256gb-us&psig=AOvVaw0ZseWjKllmdVReEqe5f_b0&ust=1681555207585000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCLiMpriXqf4CFQAAAAAdAAAAABAE'),
       (2,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.amazon.in%2FApple-MacBook-Chip-13-inch-256GB%2Fdp%2FB08N5T6CZ6&psig=AOvVaw1ZsqHYN6Kb9MgiLTAafp_A&ust=1681555223328000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCJDQ4L-Xqf4CFQAAAAAdAAAAABAJ'),
       (3,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.acer.com%2Fau-en%2Flaptops&psig=AOvVaw2IlsqAzIQqX68O8gq_uhtO&ust=1681555240580000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCJD_4seXqf4CFQAAAAAdAAAAABAE'),
       (4,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fbobbystore.kg%2Fnoutbuki_asus_vivobook_15x_oled_x1503z_i585suw1_90nb0wy1_m008v0%2F&psig=AOvVaw1d_HJOgpanzGiOC9ZaWnpl&ust=1681555251707000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCMCLns2Xqf4CFQAAAAAdAAAAABAJ'),
       (5,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fgudini.kg%2Fapple-watch-series-8-45mm-aluminium-case-gps-sport-band-midnight&psig=AOvVaw22GgK5uT74V6vvFxvxPGZ-&ust=1681555278244000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCNiV8dmXqf4CFQAAAAAdAAAAABAE');

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