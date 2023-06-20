package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.UserOrderRequest;
import com.example.gadgetariumb8.db.dto.response.*;
import com.example.gadgetariumb8.db.exception.exceptions.BadRequestException;
import com.example.gadgetariumb8.db.exception.exceptions.MessageSendingException;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.Customer;
import com.example.gadgetariumb8.db.model.Order;
import com.example.gadgetariumb8.db.model.SubProduct;
import com.example.gadgetariumb8.db.model.User;
import com.example.gadgetariumb8.db.model.enums.Status;
import com.example.gadgetariumb8.db.repository.OrderRepository;
import com.example.gadgetariumb8.db.repository.SubProductRepository;
import com.example.gadgetariumb8.db.repository.UserRepository;
import com.example.gadgetariumb8.db.repository.impl.CustomOrderRepository;
import com.example.gadgetariumb8.db.service.OrderService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CustomOrderRepository customOrderRepository;
    private final SubProductRepository subProductRepository;
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final JdbcTemplate jdbcTemplate;
    private final Configuration configuration;

    @Override
    public PaginationResponse<OrderResponse> getAllOrders(String keyWord, String status, LocalDate from, LocalDate before, int page, int pageSize) {
        return customOrderRepository.getAllOrders(keyWord, status, from, before, page, pageSize);
    }

    @Override
    public UserOrderResponse ordering(UserOrderRequest userOrderRequest) {
        List<SubProduct> subProducts = new ArrayList<>();
        BigDecimal totalPrice = new BigDecimal(0);
        int quantity = 0;
        for (Map.Entry<Long, Integer> p : userOrderRequest.productsIdAndQuantity().entrySet()) {
            SubProduct subProduct = subProductRepository.findById(p.getKey()).orElseThrow(
                    () -> new NotFoundException(String.format("Продукт с id: %s не найден!!!", p.getKey())));
            subProducts.add(subProduct);
            BigDecimal price;
            if (subProduct.getDiscount() != null && subProduct.getDiscount().getPercent() > 0) {
                BigDecimal discountPercent = BigDecimal.valueOf(subProduct.getDiscount().getPercent());
                BigDecimal discountAmount = subProduct.getPrice().multiply(discountPercent).divide(BigDecimal.valueOf(100));
                price = subProduct.getPrice().subtract(discountAmount);
            } else {
                price = subProduct.getPrice();
            }
            if (p.getValue() > 0) {
                price = price.multiply(BigDecimal.valueOf(p.getValue()));
                quantity += p.getValue();
            }
            totalPrice = totalPrice.add(price);
        }

        Customer customer = new Customer();
        customer.setFirstName(userOrderRequest.customerInfo().firstName());
        customer.setLastName(userOrderRequest.customerInfo().lastName());
        customer.setEmail(userOrderRequest.customerInfo().email());
        customer.setPhoneNumber(userOrderRequest.customerInfo().phoneNumber());
        customer.setAddress(userOrderRequest.customerInfo().address());

        SecureRandom random = new SecureRandom();
        int randomNumber = random.nextInt(1000000, 9999999);

        Order order = new Order();
        order.setDate(LocalDate.now());
        order.setQuantity(quantity);
        order.setTotalPrice(totalPrice);
        order.setStatus(Status.PENDING);
        order.setDeliveryType(userOrderRequest.deliveryType());
        order.setPaymentType(userOrderRequest.paymentType());
        order.setOrderNumber(String.valueOf(randomNumber));
        order.addAllSubProducts(subProducts);
        order.setCustomer(customer);
        getAuthenticate().addOrder(order);

        Map<String, Object> model = new HashMap<>();
        model.put("orderNumber", order.getOrderNumber());
        model.put("dateOfOrder", order.getDate());
        model.put("statusOfOrder", "В ожидании");
        model.put("datePurchase", order.getDate());
        model.put("customer", order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
        model.put("phoneNumber", order.getCustomer().getPhoneNumber());
        String deliveryType = "Самовывоз из магазина";
        if (order.isDeliveryType()) {
            deliveryType = "Доставка курьером";
        }
        model.put("deliveryType", deliveryType);
        model.put("link", "https://t.me/erkurss");

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Template template = configuration.getTemplate("order-email-template.html");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            mimeMessageHelper.setTo(order.getCustomer().getEmail());
            mimeMessageHelper.setText(html, true);
            mimeMessageHelper.setSubject("Gadgetarium");
            mimeMessageHelper.setFrom("Gadgetarium@gmail.com");
            javaMailSender.send(message);
        } catch (MessagingException | IOException | TemplateException e) {
            throw new MessageSendingException("Ошибка при отправке сообщения!");
        }

        return UserOrderResponse.builder()
                .httpStatus(HttpStatus.OK)
                .orderNumber(order.getOrderNumber())
                .message(String.format("""
                        Ваша заявка №%s от %s оформлена успешно.
                        Вся актуальная информация о статусе исполнения\s
                        заказа придет на указанный email:
                         %s""", order.getOrderNumber(), order.getDate(), order.getCustomer().getEmail()))
                .build();
    }

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
    public SimpleResponse changeStatusOfOrder(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> {
            log.error(String.format("Order with id - %s is not found!", orderId));
            throw new NotFoundException(String.format("Заказ с id - %s не найден!", orderId));
        });
        Status newStatus;
        String statusRu;
        switch (status) {
            case "В ожидании" -> {
                newStatus = Status.PENDING;
                statusRu = "В ожидании";
            }
            case "Готов к выдаче" -> {
                newStatus = Status.READY_FOR_DELIVERY;
                statusRu = "Готов к выдаче";
            }
            case "Получен" -> {
                newStatus = Status.RECEIVED;
                statusRu = "Получен";
            }
            case "Отменить" -> {
                newStatus = Status.CANCEL;
                statusRu = "Отменен";
            }
            case "Курьер в пути" -> {
                newStatus = Status.COURIER_ON_THE_WAY;
                statusRu = "Курьер в пути";
            }
            case "Доставлен" -> {
                newStatus = Status.DELIVERED;
                statusRu = "Доставлен";
            }
            default -> {
                log.error("Статус не соответствует!");
                return SimpleResponse.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("Статус не соответствует!")
                        .build();
            }
        }
        order.setStatus(newStatus);

        Map<String, Object> model = new HashMap<>();
        model.put("orderNumber", order.getOrderNumber());
        model.put("dateOfOrder", order.getDate());
        model.put("statusOfOrder", statusRu);
        model.put("datePurchase", order.getDate());
        model.put("customer", order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
        model.put("phoneNumber", order.getCustomer().getPhoneNumber());
        String deliveryType = "Самовывоз из магазина";
        if (order.isDeliveryType()) {
            deliveryType = "Доставка курьером";
        }
        model.put("deliveryType", deliveryType);
        model.put("link", "https://t.me/erkurss");
        model.put("dateOfChangeStatus", LocalDate.now());

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Template template = configuration.getTemplate("order-status-template.html");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            mimeMessageHelper.setTo(order.getCustomer().getEmail());
            mimeMessageHelper.setText(html, true);
            mimeMessageHelper.setSubject("Gadgetarium");
            mimeMessageHelper.setFrom("Gadgetarium@gmail.com");
            javaMailSender.send(message);
        } catch (MessagingException | IOException | TemplateException e) {
            log.error("Ошибка при отправке сообщения!");
            throw new MessageSendingException("Ошибка при отправке сообщения!");
        }

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Status changed successfully!")
                .build();
    }

    @Override
    public SimpleResponse delete(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NotFoundException("Заказ с id: %s не найден.".formatted(orderId)));
        if (!order.getStatus().equals(Status.DELIVERED)
                && !order.getStatus().equals(Status.CANCEL)
                && !order.getStatus().equals(Status.RECEIVED)
        ) {
            throw new BadRequestException("""
                    Заказ с id: %s не может быть удален, так как он не выполнен. Статус заказа - %s"""
                    .formatted(orderId, order.getStatus()));
        }
        orderRepository.deleteById(orderId);

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Заказ с id: %s удален.".formatted(orderId))
                .build();
    }

    @Override
    public OrderInfoResponse getOrderInfo(Long orderId) {
        String sql = """
                select o.id,
                       o.order_number,
                       o.quantity,
                       o.total_price,
                       o.status,
                       c.phone_number,
                       c.address,
                       concat(c.first_name, ' ', c.last_name) as fullName
                from orders o
                         join customers c on c.id = o.customer_id
                where o.id = ?;
                """;

        OrderInfoResponse orderInfoResponse = jdbcTemplate.query(sql, (resultSet, i) -> new OrderInfoResponse(
                resultSet.getLong("id"),
                resultSet.getInt("order_number"),
                resultSet.getInt("quantity"),
                resultSet.getBigDecimal("total_price"),
                resultSet.getString("status"),
                resultSet.getString("phone_number"),
                resultSet.getString("address"),
                resultSet.getString("fullName")
        ), orderId).stream().findFirst().orElseThrow(() -> new NotFoundException("Order by id %s is not found.".formatted(orderId)));
        String sql2 = """
                select concat(b.name, ' ', p.name, ' ', sp.colour) as name,
                       sp.price,
                       d.percent,
                       (d.percent * sp.price/ 100) sumOfDiscount
                from orders o
                         join orders_sub_products osp on o.id = osp.order_id
                         join sub_products sp on sp.id = osp.sub_products_id
                         join products p on p.id = sp.product_id
                         join brands b on b.id = p.brand_id
                         left join discounts d on d.id = sp.discount_id
                where o.id = ?
                """;

        List<OrderProductResponse> query = jdbcTemplate.query(sql2, (resultSet, i) -> new OrderProductResponse(
                resultSet.getString("name"),
                resultSet.getBigDecimal("price"),
                resultSet.getInt("percent"),
                resultSet.getBigDecimal("sumOfDiscount")
        ), orderId);
        orderInfoResponse.setProducts(query);
        return orderInfoResponse;
    }
}
