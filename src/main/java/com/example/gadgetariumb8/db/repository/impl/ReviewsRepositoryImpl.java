package com.example.gadgetariumb8.db.repository.impl;

import com.example.gadgetariumb8.db.dto.response.FeedbackResponse;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.repository.ReviewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ReviewsRepositoryImpl implements ReviewsRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public FeedbackResponse getFeedbacks(Long productId) {
        String sql = """
                SELECT
                    (SELECT COUNT(*) FROM reviews r WHERE r.product_id = p.id AND grade = 5) AS five,
                    (SELECT COUNT(*) FROM reviews r WHERE r.product_id = p.id AND grade = 4) AS four,
                    (SELECT COUNT(*) FROM reviews r WHERE r.product_id = p.id AND grade = 3) AS three,
                    (SELECT COUNT(*) FROM reviews r WHERE r.product_id = p.id AND grade = 2) AS two,
                    (SELECT COUNT(*) FROM reviews r WHERE r.product_id = p.id AND grade = 1) AS one,
                    (SELECT AVG(r.grade) FROM reviews r WHERE r.product_id = p.id) as rating,
                    (SELECT COUNT(*) from reviews r WHERE r.product_id = p.id) as total_reviews
                FROM products p where id = ?;
                                """;
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            FeedbackResponse feedbackResponse = new FeedbackResponse();
            feedbackResponse.setOne(resultSet.getInt("one"));
            feedbackResponse.setTwo(resultSet.getInt("two"));
            feedbackResponse.setFour(resultSet.getInt("four"));
            feedbackResponse.setFive(resultSet.getInt("five"));
            feedbackResponse.setRating(resultSet.getDouble("rating"));
            feedbackResponse.setTotalReviews(resultSet.getInt("total_reviews"));
            return feedbackResponse;
        }, productId).stream().findAny().orElseThrow(() -> new NotFoundException("Find first index is not found!!!"));


    }
}
