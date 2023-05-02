package com.example.gadgetariumb8.db.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class CustomOrderRepository {
    public String getAllOrder() {
        return """
                select o.id as id,concat(c.first_name,' ',c.last_name) as fio,
                      o.order_number as orderNumber,
                      o.date as createdAt, o.quantity as quantity, o.total_price as totalPrice,o.delivery_type as deliveryType,
                      o.status as status from orders o join customers c on o.customer_id = c.id where o.status = ?
                      %s %s
                """;
    }

    public String dateClauseFromBefore() {
        return """
                AND o.date BETWEEN '%s' AND '%s'
                """;
    }

    public String dateClauseFrom() {
        return """
                AND o.date >= '%s'
                """;
    }

    public String dateClauseBefore() {
        return """
                AND o.date <= '%s'
                """;
    }

    public String keyWordCondition() {
        return """
                and (c.first_name ilike ? or c.last_name ilike ? or o.order_number ilike ?
                  or cast(o.total_price as text) ilike ?
                  or o.status ilike ?)
                """;
    }

    public String limitOffset() {
        return """
                LIMIT ? OFFSET ?
                """;
    }

    public String countSql(String sql) {
        return " SELECT COUNT(*) FROM (" + sql + ") AS count_query";
    }
}
