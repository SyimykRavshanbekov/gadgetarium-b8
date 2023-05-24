package com.example.gadgetariumb8.db.repository;

import com.example.gadgetariumb8.db.dto.response.FeedbackInfographic;
import com.example.gadgetariumb8.db.dto.response.FeedbackResponse;

public interface ReviewsRepository {
    FeedbackResponse getFeedbacks(Long productId);

    FeedbackInfographic getFeedbackInfographic();
}
