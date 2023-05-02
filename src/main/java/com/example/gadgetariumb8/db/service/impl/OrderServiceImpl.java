package com.example.gadgetariumb8.db.service.impl;


import com.example.gadgetariumb8.db.dto.response.OrderResponse;
import com.example.gadgetariumb8.db.dto.response.PaginationResponse;
import com.example.gadgetariumb8.db.exception.exceptions.BadRequestException;
import com.example.gadgetariumb8.db.repository.CustomOrderRepository;
import com.example.gadgetariumb8.db.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class  OrderServiceImpl implements OrderService {
    private final JdbcTemplate jdbcTemplate;
    private final CustomOrderRepository customOrderRepository;
    @Override
    public PaginationResponse<OrderResponse> getAllOrders(String keyWord, String status, LocalDate from, LocalDate before, int page, int pageSize) {
        String sql = customOrderRepository.getAllOrder();

        String dateClause = "";
        if (from != null && before != null) {
            if (from.isAfter(before)) {
                throw new BadRequestException("The from date must be earlier than the date before");
            } else if (from.isAfter(LocalDate.now()) || before.isAfter(LocalDate.now())) {
                throw new BadRequestException("The date must be in the past tense");
            }
            dateClause = customOrderRepository.dateClauseFromBefore();
        } else if (from != null) {
            if (from.isAfter(LocalDate.now())) {
                throw new BadRequestException("The date must be in the past tense");
            }
            dateClause = customOrderRepository.dateClauseFrom();
        } else if (before != null) {
            if (before.isAfter(LocalDate.now())) {
                throw new BadRequestException("The date must be in the past tense");
            }
            dateClause = customOrderRepository.dateClauseBefore();
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
            keyWordCondition = customOrderRepository.keyWordCondition();
        }
        sql = String.format(sql, dateClause, keyWordCondition);

        int count = jdbcTemplate.queryForObject(customOrderRepository.countSql(sql), params.toArray(), Integer.class);
        int totalPage = (int) Math.ceil((double) count / pageSize);

        sql = sql + customOrderRepository.limitOffset();
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

        return PaginationResponse.<OrderResponse>builder()
                .foundProducts(count)
                .elements(orders)
                .currentPage(page)
                .totalPages(totalPage)
                .build();
    }
}
