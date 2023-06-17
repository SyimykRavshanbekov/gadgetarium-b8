package com.example.gadgetariumb8.db.repository.impl;

import org.springframework.stereotype.Repository;

@Repository
public class CustomProductRepository {

    public String countCompare () {
        return """
                select c.name as categoryName,count(uc.comparisons_id) as countComparisons from
                users_comparisons uc join sub_products sp on uc.comparisons_id = sp.id
                join products p on sp.product_id = p.id join sub_categories sc on sc.id = p.sub_category_id
                join categories c on c.id = sc.category_id where uc.user_id = ?
                group by c.name;
                """;
    }
}
