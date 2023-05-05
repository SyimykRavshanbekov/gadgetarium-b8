package com.example.gadgetariumb8.db.repository;

import org.springframework.stereotype.Repository;

@Repository
public class UserCustomRepository {

    public String getAllChosenOne(){
        return """
                select us.id as id,
                spi.images as imageProduct,
                p.name as productName,
                p.rating as rating,
                sp.price as priceProduct
                 from users us
                        join users_favorites uf on us.id = uf.user_id
                        join sub_products sp on sp.id = uf.favorites_id
                        join products p on p.id = sp.product_id
                        join sub_product_images spi on sp.id = spi.sub_product_id where us.id= ?
                """;
    }
}
