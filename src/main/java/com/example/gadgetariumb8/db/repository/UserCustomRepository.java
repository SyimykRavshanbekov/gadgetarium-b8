package com.example.gadgetariumb8.db.repository;

import org.springframework.stereotype.Repository;

@Repository
public class UserCustomRepository {

    public String getAllChosenOne(){
        return """
                select uf.user_id as userId,
                       (select spi.images from sub_product_images spi where spi.sub_product_id = sp.id limit 1) as productImage,
                       p.name as productName,
                       p.rating productRating,
                       sp.price as productPrice
                from categories c
                join sub_categories sc on c.id = sc.category_id
                join products p on sc.id = p.sub_category_id
                join sub_products sp on p.id = sp.product_id
                join users_favorites uf on sp.id = uf.favorites_id
                where uf.user_id = ?
                """;
    }
}
