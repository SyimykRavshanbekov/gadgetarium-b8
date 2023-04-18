package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.request.ReviewRequest;
import com.example.gadgetariumb8.db.dto.response.ReviewResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;

import java.util.List;

/**
 * name : kutman
 **/
public interface ReviewService {
    SimpleResponse saveReview(ReviewRequest newRequest,Long userId,Long productId);
    ReviewResponse getByIdReview(Long id);
    SimpleResponse deleteById(Long id);
    SimpleResponse updateReview(Long id,ReviewRequest newRequest);
    List<ReviewResponse>getAllReview(Long productId);
}
