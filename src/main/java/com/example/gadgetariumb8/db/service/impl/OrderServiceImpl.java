package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.UserOrderRequest;
import com.example.gadgetariumb8.db.dto.response.OrderResponse;
import com.example.gadgetariumb8.db.dto.response.PaginationResponse;
import com.example.gadgetariumb8.db.dto.response.UserOrderResponse;
import com.example.gadgetariumb8.db.exception.exceptions.BadRequestException;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.Customer;
import com.example.gadgetariumb8.db.model.Order;
import com.example.gadgetariumb8.db.model.SubProduct;
import com.example.gadgetariumb8.db.repository.CustomOrderRepository;
import com.example.gadgetariumb8.db.repository.SubProductRepository;
import com.example.gadgetariumb8.db.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final JdbcTemplate jdbcTemplate;
    private final CustomOrderRepository customOrderRepository;
    private final SubProductRepository subProductRepository;

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
        if (keyWord != null) {
            params.add("%" + keyWord + "%");
            params.add("%" + keyWord + "%");
            params.add("%" + keyWord + "%");
            params.add("%" + keyWord + "%");
            params.add("%" + keyWord + "%");
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

    @Override
    public UserOrderResponse ordering(UserOrderRequest userOrderRequest) {
        List<SubProduct> subProducts = userOrderRequest.productsIdAndQuantity().keySet()
                .stream()
                .map(id -> subProductRepository.findById(id).orElseThrow(
                        () -> new NotFoundException(String.format("Product with id %s is not found!", id))))
                .collect(Collectors.toMap());

        BigDecimal totalPrice = new BigDecimal(0);
        for (Map.Entry<Long, Integer> p : userOrderRequest.productsIdAndQuantity().entrySet()) {
            SubProduct subProduct = subProductRepository.findById(p.getKey()).orElseThrow(
                    () -> new NotFoundException(String.format("Product with id %s is not found!", p.getKey())));
            BigDecimal price = new BigDecimal(0);
            if (subProduct.getDiscount() != null) {
                price = subProduct.getPrice().subtract(subProduct.getPrice()
                        .multiply(BigDecimal.valueOf(subProduct.getDiscount().getPercent()))
                        .divide(BigDecimal.valueOf(100)));
            } else {
                price = subProduct.getPrice();
            }
            price = totalPrice.multiply(BigDecimal.valueOf(p.getValue()).multiply(BigDecimal.valueOf(1)));
        }
        Customer customer = new Customer();
        customer.setFirstName(userOrderRequest.customerInfo().firstName());
        customer.setLastName(userOrderRequest.customerInfo().lastName());
        customer.setEmail(userOrderRequest.customerInfo().email());
        customer.setPhoneNumber(userOrderRequest.customerInfo().phoneNumber());
        customer.setAddress(userOrderRequest.customerInfo().address());

        Order order = new Order();
        order.setDate(LocalDate.now());

        int quantity = 0;
        for (Integer value : userOrderRequest.productsIdAndQuantity().values()) {
            quantity += value;
        }
        order.setQuantity(quantity);

        for (SubProduct subProduct : subProducts) {
            subProduct.getPrice()
        }
        order.setTotalPrice();

        SecureRandom random = new SecureRandom();
        int randomNumber = random.nextInt(9000000) + 1000000;
        UUID uuid = new UUID(0, randomNumber);

        order.setOrderNumber(String.valueOf(uuid));

        return null;
    }
}
