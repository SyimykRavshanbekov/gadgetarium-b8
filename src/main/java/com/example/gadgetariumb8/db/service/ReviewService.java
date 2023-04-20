package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.response.ReviewResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;

import java.util.List;

public interface ReviewService {
    SimpleResponse deleteById(Long id);
    SimpleResponse replyToFeedback(String answer,Long id);
    List<ReviewResponse>getAllReview(String param);

}
