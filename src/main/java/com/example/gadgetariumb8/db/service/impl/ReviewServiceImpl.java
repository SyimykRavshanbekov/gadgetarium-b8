package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.AnswerRequest;
import com.example.gadgetariumb8.db.dto.response.AdminReviewsResponse;
import com.example.gadgetariumb8.db.dto.response.FeedbackResponse;
import com.example.gadgetariumb8.db.dto.response.ReviewResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
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
                    return new NotFoundException(String.format("Review with id %s not found", id));
                });
        reviewRepository.deleteById(review.getId());
        log.info(String.format("Review with id %s deleted", id));
        return SimpleResponse.builder().message(String.format("Review with id %s deleted", id)).httpStatus(HttpStatus.OK).build();
    }

    @Override
    public SimpleResponse replyToFeedback(AnswerRequest answerRequest) {
        Review review = reviewRepository.findById(answerRequest.reviewId())
                .orElseThrow(() -> {
                    log.error(String.format("Review with id %s does not exists", answerRequest.reviewId()));
                    return new NotFoundException(String.format("Review with id %s does not exists", answerRequest.reviewId()));
                });
        if (review.getAnswer() == null) {
            review.setAnswer(answerRequest.answer());
            reviewRepository.save(review);
            log.info("answer successfully saved");
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("answer successfully saved")
                    .build();
        } else {
            log.info(String.format("Review with id - %s has already been answered", answerRequest.reviewId()));
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(String.format("Review with id - %s has already been answered", answerRequest.reviewId()))
                    .build();
        }
    }

    @Override
    public Object getAllReview(String param) {
        log.info("Getting all reviews");
        String sql = null;
        String countSql = null;
        switch (param) {
            case "Unanswered" -> {
                sql = """
                        SELECT r.id                                   as id,
                               (select i.images from sub_product_images i where i.sub_product_id = sp.id limit 1)
                                                                      as product_image,
                               sp.item_number                         as item_number,
                               r.commentary                           as commentary,
                               r.grade                                as grade,
                               r.answer                               as answer,
                               ri                                     as images,
                               concat(u.first_name, ' ', u.last_name) as user_name,
                               ui.email                               as email,
                               u.image                                as user_image,
                               r.created_at_time                      as date,
                               p.name                                 as product_name
                        FROM reviews r
                                 JOIN products p on p.id = r.product_id
                                 JOIN sub_products sp on r.product_id = sp.product_id
                                 JOIN review_images ri on r.id = ri.review_id
                                 JOIN users u on u.id = r.user_id
                                 JOIN users_info ui on ui.id = r.user_id where r.answer is not null
                        """;
                countSql = """
                        SELECT count(r) as count FROM reviews r WHERE r.answer IS NOT NULL
                        """;
            }
            case "Answered" -> {
                sql = """
                        SELECT r.id                                   as id,
                               (select i.images from sub_product_images i where i.sub_product_id = sp.id limit 1)
                                                                      as product_image,
                               sp.item_number                         as item_number,
                               r.commentary                           as commentary,
                               r.grade                                as grade,
                               r.answer                               as answer,
                               ri                                     as images,
                               concat(u.first_name, ' ', u.last_name) as user_name,
                               ui.email                               as email,
                               u.image                                as user_image,
                               r.created_at_time                      as date,
                               p.name                                 as product_name
                        FROM reviews r
                                 JOIN products p on p.id = r.product_id
                                 JOIN sub_products sp on r.product_id = sp.product_id
                                 JOIN review_images ri on r.id = ri.review_id
                                 JOIN users u on u.id = r.user_id
                                 JOIN users_info ui on ui.id = r.user_id where r.answer is null
                        """;
                countSql = """
                        SELECT count(r) as count FROM reviews r WHERE r.answer IS NULL
                        """;
            }
            case "AllReviews" -> {
                sql = """
                        SELECT r.id                                   as id,
                               (select i.images from sub_product_images i where i.sub_product_id = sp.id limit 1)
                                                                      as product_image,
                               sp.item_number                         as item_number,
                               r.commentary                           as commentary,
                               r.grade                                as grade,
                               r.answer                               as answer,
                               ri                                     as images,
                               concat(u.first_name, ' ', u.last_name) as user_name,
                               ui.email                               as email,
                               u.image                                as user_image,
                               r.created_at_time                      as dates,
                               p.name                                 as product_name
                        FROM reviews r
                                 JOIN products p on p.id = r.product_id
                                 JOIN sub_products sp on r.product_id = sp.product_id
                                 JOIN review_images ri on r.id = ri.review_id
                                 JOIN users u on u.id = r.user_id
                                 JOIN users_info ui on ui.id = r.user_id
                        """;
                countSql = """
                        SELECT count(r) as count FROM reviews r
                        """;
            }
        }
        if (sql != null) {
            log.info("All reviews are got!");
            return new AdminReviewsResponse(
                    jdbcTemplate.query(sql, (resultSet, i)
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
        } else {
            return SimpleResponse
                    .builder()
                    .message("""
                            You entered a wrong word
                            The request param must accept
                            the word -(AllReviews, Answered, Unanswered)!!!
                            """)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
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
                    return new NotFoundException(String.format("Review with id %s not found", id));
                });
        if (!review.getAnswer().isEmpty()) {
            if (!answer.isBlank()) {
                review.setAnswer(answer);
                reviewRepository.save(review);
                return SimpleResponse
                        .builder()
                        .message("Answer successfully updated!")
                        .httpStatus(HttpStatus.OK)
                        .build();
            } else {
                return SimpleResponse
                        .builder()
                        .message("Response is empty !!!")
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build();
            }
        } else {
            return SimpleResponse
                    .builder()
                    .message("Comment with id no reply !!!")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }
}
