package com.example.gadgetariumb8.db.service.impl;


import com.example.gadgetariumb8.db.dto.response.OrderResponse;
import com.example.gadgetariumb8.db.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public List<OrderResponse> getAllOrders(String keyWord, String status, LocalDate from, LocalDate before, int page, int pageSize) {
        String sql = """
               select o.id as id,concat(c.first_name,' ',c.last_name) as fio,
                      o.order_number as orderNumber,
                      o.date as createdAt, o.quantity as quantity, o.total_price as totalPrice,o.delivery_type as deliveryType,
                      o.status as status from orders o join customers c on o.customer_id = c.id where o.status = ?
                      %s %s
                """;

        String dateClause = "";
        if (from != null && before != null) {
            dateClause = String.format("AND p.date BETWEEN '%s' AND '%s'", from, before);
        } else if (from != null) {
            dateClause = "AND p.date >= '%s'".formatted(from);
        } else if (before != null) {
            dateClause = "AND p.date <= '%s'".formatted(before);
        }
        String keyWordCondition = "";
        List<Object> params = new ArrayList<>();
        params.add(status);
        if (keyWord != null){
            params.add("%"+keyWord+"%");
            params.add("%"+keyWord+"%");
            params.add("%"+keyWord+"%");
            params.add("%"+keyWord+"%");
            params.add("%"+keyWord+"%");
            keyWordCondition = """
                    and (c.first_name ilike ? or c.last_name ilike ? or o.order_number ilike ?
                    or cast(o.total_price as text) ilike ?
                    or o.status ilike ?)
                    """;
        }
        sql = String.format(sql, dateClause, keyWordCondition);
        List<OrderResponse> orders = jdbcTemplate.query(sql, params.toArray(), (resultSet, i) -> new OrderResponse(
                resultSet.getLong("id"),
                resultSet.getString("fio"),
                resultSet.getString("orderNumber"),
                LocalDate.parse(resultSet.getString("createdAt")),
                resultSet.getInt("quantity"),
                resultSet.getBigDecimal("totalPrice"),
                resultSet.getBoolean("deliveryType"),
                resultSet.getString("status")
        ));

        return orders;
    }
}
