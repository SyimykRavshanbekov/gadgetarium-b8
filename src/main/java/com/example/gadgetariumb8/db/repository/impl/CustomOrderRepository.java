package com.example.gadgetariumb8.db.repository.impl;

import com.example.gadgetariumb8.db.dto.response.OrderResponse;
import com.example.gadgetariumb8.db.dto.response.PaginationResponse;
import com.example.gadgetariumb8.db.exception.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomOrderRepository {
    private final JdbcTemplate jdbcTemplate;

    public PaginationResponse<OrderResponse> getAllOrders(String keyWord, String status, LocalDate from, LocalDate before, int page, int pageSize) {
        String sql = """
                select o.id as id,concat(c.first_name,' ',c.last_name) as fio,
                      o.order_number as orderNumber,
                      o.date as createdAt, o.quantity as quantity, o.total_price as totalPrice,o.delivery_type as deliveryType,
                      o.status as status from orders o join customers c on o.customer_id = c.id where o.status = ?
                      %s %s
                      ORDER BY o.id DESC
                """;
        log.info("Получение всех заказов.");

        String dateClause = "";
        if (from != null && before != null) {
            if (from.isAfter(before)) {
                log.error("Дата с должна быть раньше, чем дата до");
                throw new BadRequestException("The from date must be earlier than the date before");
            } else if (from.isAfter(LocalDate.now()) || before.isAfter(LocalDate.now())) {
                log.error("The date must be in the past tense");
                throw new BadRequestException("The date must be in the past tense");
            }
            dateClause = "AND o.date BETWEEN '%s' AND '%s'".formatted(from, before);
        } else if (from != null) {
            if (from.isAfter(LocalDate.now())) {
                log.error("The date must be in the past tense");
                throw new BadRequestException("The date must be in the past tense");
            }
            dateClause = "AND o.date >= '%s'".formatted(from);
        } else if (before != null) {
            if (before.isAfter(LocalDate.now())) {
                log.error("The date must be in the past tense");
                throw new BadRequestException("The date must be in the past tense");
            }
            dateClause = "AND o.date <= '%s'".formatted(before);
        }
        String keyWordCondition = "";
        List<Object> params = new ArrayList<>();
        params.add(status);
        if (keyWord != null) {
            params.add("%" + keyWord + "%");
            params.add("%" + keyWord + "%");
            params.add("%" + keyWord + "%");
            params.add("%" + keyWord + "%");
            params.add("%" + keyWord + "%");
            keyWordCondition = """
                    and (c.first_name ilike ? or c.last_name ilike ? or o.order_number ilike ?
                      or cast(o.total_price as text) ilike ?
                      or o.status ilike ?)
                    """;
        }
        sql = String.format(sql, dateClause, keyWordCondition);

        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM (" + sql + ") AS count_query", params.toArray(), Integer.class);
        int totalPage = (int) Math.ceil((double) count / pageSize);

        sql = sql + "LIMIT ? OFFSET ?";
        int offset = (page - 1) * pageSize;
        params.add(pageSize);
        params.add(offset);
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
        log.info("Orders are successfully got!");
        return PaginationResponse.<OrderResponse>builder()
                .countOfElements(count)
                .elements(orders)
                .currentPage(page)
                .totalPages(totalPage)
                .build();
    }
}
