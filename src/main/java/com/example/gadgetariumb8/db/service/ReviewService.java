package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.request.AnswerRequest;
import com.example.gadgetariumb8.db.dto.request.FeedbackRequest;
import com.example.gadgetariumb8.db.dto.response.AdminReviewsResponse;
import com.example.gadgetariumb8.db.dto.response.FeedbackInfographic;
import com.example.gadgetariumb8.db.dto.response.FeedbackResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;

public interface ReviewService {

    SimpleResponse deleteById(Long id);

    SimpleResponse replyToFeedback(AnswerRequest answerRequest);

    AdminReviewsResponse getAllReview(String param);

    FeedbackResponse getFeedbacks(Long productId);

    SimpleResponse updateFeedback(Long id, String feedback);

    FeedbackInfographic getFeedbackInfographic();

    SimpleResponse post(FeedbackRequest feedbackRequest);
}
