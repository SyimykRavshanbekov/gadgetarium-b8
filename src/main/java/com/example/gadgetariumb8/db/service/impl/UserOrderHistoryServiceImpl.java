package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.response.OrderHistoryResponse;
import com.example.gadgetariumb8.db.dto.response.ProductsResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.dto.response.UserOrderHistoryResponse;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.Order;
import com.example.gadgetariumb8.db.model.SubProduct;
import com.example.gadgetariumb8.db.model.User;
import com.example.gadgetariumb8.db.repository.UserRepository;
import com.example.gadgetariumb8.db.service.UserOrderHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserOrderHistoryServiceImpl implements UserOrderHistoryService {

    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;

    private User getAuthenticate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        log.info("Токен взят!");
        return userRepository.findUserInfoByEmail(login).orElseThrow(() -> {
            log.error("Пользователь не найден с токеном пожалуйста войдите или зарегистрируйтесь!");
            return new NotFoundException("пользователь не найден с токеном пожалуйста войдите или зарегистрируйтесь");
        }).getUser();
    }

    @Override
    public List<OrderHistoryResponse> getOrderHistory() {
        User user = getAuthenticate();
        String sql = """
                    SELECT o.id,o.total_price,o.status,o.order_number,o.date
                    FROM Users u JOIN orders o ON u.id = o.user_id
                    WHERE user_id = ?
                """;
        return jdbcTemplate.query(sql,
                new Object[]{user.getId()},
                (resultSet, item) ->
                        OrderHistoryResponse.builder()
                                .order_id(resultSet.getLong("id"))
                                .date(resultSet.getDate("date").toLocalDate())
                                .orderNumber(resultSet.getString("order_number"))
                                .status(resultSet.getString("status"))
                                .totalPrice(resultSet.getBigDecimal("total_price"))
                                .build()
        );
    }

    @Override
    public SimpleResponse deleteOrderHistory() {
        User user = getAuthenticate();
        List<Order> userOrders = jdbcTemplate.query("select * from orders where user_id = ?",
                new Object[]{user.getId()},
                new BeanPropertyRowMapper<>(Order.class));
        String sql = """
                DELETE FROM orders_sub_products WHERE order_id = ?
                """;
        for (Order order : userOrders) {
            jdbcTemplate.update(sql, order.getId());
        }
        jdbcTemplate.update("DELETE FROM orders where user_id = ?", user.getId());
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Заказы пользователя успешно удалены.")
                .build();
    }

    @Override
    public UserOrderHistoryResponse getUserOrderById(Long order_id) {
        User user = getAuthenticate();
        List<Order> userOrders = user.getOrders();
        Order order = userOrders
                .stream()
                .filter(item -> Objects.equals(item.getId(), order_id))
                .findAny()
                .orElseThrow(() -> new NotFoundException("Заказ с id:"+order_id+" не найден!"));

        List<SubProduct> subProducts = order.getSubProducts();
        List<ProductsResponse> newProductResponse = new ArrayList<>();
        BigDecimal discountPrice = new BigDecimal("0");
        BigDecimal totalPrice = new BigDecimal("0");
        for (SubProduct item : subProducts) {
            newProductResponse.add(ProductsResponse
                    .builder()
                    .image(item.getImages().stream().findFirst().orElse(null))
                    .subProductId(item.getId())
                    .price(item.getPrice())
                    .productInfo(item.getProduct().getName()
                            + " " + item.getCharacteristics().get("память")
                            + "GB " + item.getColour())
                    .quantity(item.getProduct().getReviews().size())
                    .rating(item.getProduct().getRating())
                    .discount(item.getDiscount().getPercent())
                    .build());
            totalPrice = totalPrice.add(item.getPrice());
            discountPrice = discountPrice.add(item.getPrice()
                    .multiply(BigDecimal.valueOf(item.getDiscount().getPercent()))
                    .divide(BigDecimal.valueOf(100)));
        }

        String[] address = order.getCustomer().getAddress().split(",");

        Queue<String> addr = new LinkedList<>();
        Arrays.stream(address).forEach(addr::offer);

        return UserOrderHistoryResponse
                .builder()
                .orderNumber(order.getOrderNumber())
                .orderedProducts(newProductResponse)
                .status(order.getStatus().toString())
                .client(order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName())
                .firstName(order.getCustomer().getFirstName())
                .lastName(order.getCustomer().getLastName())
                .region(addr.poll())
                .city(addr.poll())
                .address(addr.poll())
                .email(order.getCustomer().getEmail())
                .payment_type(order.getPaymentType().toString())
                .tel_number(order.getCustomer().getPhoneNumber())
                .date(order.getDate())
                .discountPrice(discountPrice)
                .totalPrice(totalPrice.subtract(discountPrice))
                .build();
    }
}