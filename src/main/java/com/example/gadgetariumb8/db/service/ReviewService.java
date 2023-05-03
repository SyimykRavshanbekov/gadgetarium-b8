package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.request.AnswerRequest;
import com.example.gadgetariumb8.db.dto.response.ReviewResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;

import java.util.List;

public interface ReviewService {

    SimpleResponse deleteById(Long id);

    SimpleResponse replyToFeedback(AnswerRequest answerRequest);

    List<ReviewResponse> getAllReview(String param);

}
