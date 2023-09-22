package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.AnswerRequest;
import com.example.gadgetariumb8.db.dto.request.FeedbackRequest;
import com.example.gadgetariumb8.db.dto.response.*;
import com.example.gadgetariumb8.db.exception.exceptions.BadRequestException;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.Order;
import com.example.gadgetariumb8.db.model.Review;
import com.example.gadgetariumb8.db.model.SubProduct;
import com.example.gadgetariumb8.db.model.User;
import com.example.gadgetariumb8.db.model.enums.Status;
import com.example.gadgetariumb8.db.repository.ProductRepository;
import com.example.gadgetariumb8.db.repository.ReviewRepository;
import com.example.gadgetariumb8.db.repository.ReviewsRepository;
import com.example.gadgetariumb8.db.repository.UserRepository;
import com.example.gadgetariumb8.db.service.ReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewsRepository reviewsRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public SimpleResponse deleteById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(String.format("Review with id %s not found", id));
                    return new NotFoundException(String.format("Отзыв с id: %s не найден", id));
                });
        reviewRepository.deleteById(review.getId());
        log.info(String.format("Reviews with id %s deleted", id));
        return SimpleResponse.builder().message(String.format("Отзывы с id: %s удален", id)).httpStatus(HttpStatus.OK).build();
    }

    @Override
    public SimpleResponse replyToFeedback(AnswerRequest answerRequest) {
        Review review = reviewRepository.findById(answerRequest.reviewId())
                .orElseThrow(() -> {
                    log.error(String.format("Review with id %s does not exists", answerRequest.reviewId()));
                    return new NotFoundException(String.format("Отзыв с id: %s не существует", answerRequest.reviewId()));
                });
        if (review.getAnswer() == null) {
            review.setAnswer(answerRequest.answer());
            reviewRepository.save(review);
            log.info("answer successfully saved");
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Ответ успешно сохранен!")
                    .build();
        } else {
            log.info(String.format("Review with id - %s has already been answered", answerRequest.reviewId()));
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(String.format("Отзыв с id: — %s  уже ответили", answerRequest.reviewId()))
                    .build();
        }
    }

    @Override
    public AdminReviewsResponse getAllReview(String param) {
        String query = """
                SELECT r.id                                                         AS id,
                      (select spi.images
                         from sub_product_images spi
                         join sub_products sp on spi.sub_product_id = sp.id
                         join products prod on sp.product_id = prod.id
                         where prod.id = p.id
                         limit 1
                      )                                                             AS product_image,
                      (select sp.item_number from sub_products sp
                          join products prod on sp.product_id = prod.id
                          where prod.id = p.id
                          limit 1
                      )                                                             AS item_number,
                      r.commentary                                                  AS commentary,
                      r.grade                                                       AS grade,
                      r.answer                                                      AS answer,
                      CONCAT(u.first_name, ' ', u.last_name)                        AS user_name,
                      ui.email                                                      AS email,
                      u.image                                                       AS user_image,
                      r.created_at_time                                             AS dates,
                      p.name                                                        AS product_name
                FROM reviews r
                      JOIN products p ON p.id = r.product_id
                      JOIN users u ON u.id = r.user_id
                      JOIN users_info ui ON ui.id = u.user_info_id %s
                      ORDER BY r.id DESC
                """;

        String statusCondition = switch (param) {
            case "Unanswered" -> " where r.answer IS NULL";
            case "Answered" -> " where r.answer IS NOT NULL";
            default -> "";
        };

        query = query.formatted(statusCondition);
        String countSql = "SELECT count(r) as count FROM reviews r WHERE r.answer IS NULL";

        log.info("Getting all reviews");

        AdminReviewsResponse response = new AdminReviewsResponse(
                jdbcTemplate.query(query, (resultSet, i)
                                -> new ReviewResponse(
                                resultSet.getLong("id"),
                                resultSet.getString("product_image"),
                                resultSet.getInt("item_number"),
                                resultSet.getString("commentary"),
                                resultSet.getInt("grade"),
                                resultSet.getString("answer"),
                                reviewRepository.getAllImage(resultSet.getLong("id")),
                                resultSet.getString("user_name"),
                                resultSet.getString("email"),
                                resultSet.getString("user_image"),
                                resultSet.getString("dates"),
                                resultSet.getString("product_name")

                        )
                ),
                jdbcTemplate.queryForObject(countSql, Integer.class)
        );
        return response;
    }

    @Override
    public FeedbackResponse getFeedbacks(Long productId) {
        return reviewsRepository.getFeedbacks(productId);
    }

    @Override
    public SimpleResponse updateFeedback(Long id, String answer) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(String.format("Review with id %s not found", id));
                    return new NotFoundException(String.format("Отзыв с id: %s не найден", id));
                });
        if (!review.getAnswer().isEmpty()) {
            if (!answer.isBlank()) {
                review.setAnswer(answer);
                reviewRepository.save(review);
                return SimpleResponse
                        .builder()
                        .message("Ответ успешно обновлен!")
                        .httpStatus(HttpStatus.OK)
                        .build();
            } else {
                return SimpleResponse
                        .builder()
                        .message("Ответ пустой!!!")
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build();
            }
        } else {
            return SimpleResponse
                    .builder()
                    .message("Комментарий с ID без ответа !!!")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Override
    public FeedbackInfographic getFeedbackInfographic() {
        return reviewsRepository.getFeedbackInfographic();
    }

    @Override
    public SimpleResponse post(FeedbackRequest feedbackRequest) {
        User user = getAuthenticate();
        List<Review> reviews = reviewRepository.findAll();
        for (Review review : reviews) {
            if (review.getUser().getId().equals(user.getId()) && review.getProduct().getId().equals(feedbackRequest.productId())) {
                throw new BadRequestException("Вы уже оставили отзыв на этот продукт");
            }
        }

        boolean hasPurchasedProduct = false;
        for (Order order : user.getOrders()) {
            for (SubProduct subProduct1 : order.getSubProducts()){
                if (subProduct1.getProduct().getId().equals(feedbackRequest.productId())) {
                    hasPurchasedProduct = true;
                    break;
                }
            }
        }

        if (!hasPurchasedProduct) {
            throw new BadRequestException("Чтобы оставить отзыв, купите продукт");
        }

        for (Order order : user.getOrders()) {
            for (SubProduct subProduct : order.getSubProducts()) {
                if (subProduct.getProduct().getId().equals(feedbackRequest.productId())) {
                    if (order.getStatus() != Status.DELIVERED && order.getStatus() != Status.RECEIVED) {
                        throw new BadRequestException("Чтобы оставить отзыв, продукт должен быть доставлен или получен");
                    }
                }
            }
        }

        Review review = new Review();
        review.setGrade(feedbackRequest.grade());
        review.setCommentary(feedbackRequest.comment());
        review.setCreatedAtTime(LocalDateTime.now());
        review.setUser(getAuthenticate());
        review.setProduct(productRepository.findById(feedbackRequest.productId()).orElseThrow(() -> new NotFoundException("Product not found!")));
        review.addImages(feedbackRequest.images());
        reviewRepository.save(review);

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Ваш отзыв был успешно отправлен!")
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
}
