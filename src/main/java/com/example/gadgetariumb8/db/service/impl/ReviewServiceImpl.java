package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.AnswerRequest;
import com.example.gadgetariumb8.db.dto.response.*;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.Review;
import com.example.gadgetariumb8.db.repository.ReviewRepository;
import com.example.gadgetariumb8.db.repository.ReviewsRepository;
import com.example.gadgetariumb8.db.service.ReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReviewServiceImpl implements ReviewService {

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
}
