package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.ReviewRequest;
import com.example.gadgetariumb8.db.dto.response.ReviewResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.model.Review;
import com.example.gadgetariumb8.db.repository.ReviewRepository;
import com.example.gadgetariumb8.db.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * name : kutman
 **/
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    @Override
    public SimpleResponse saveReview(ReviewRequest newRequest, Long userId, Long productId) {
        reviewRepository.save(new Review(newRequest.commentary(), newRequest.grade(),newRequest.images()));
        return new SimpleResponse("save", HttpStatus.OK);
    }

    @Override
    public ReviewResponse getByIdReview(Long id) {
        return null;
    }

    @Override
    public SimpleResponse deleteById(Long id) {
        return null;
    }

    @Override
    public SimpleResponse updateReview(Long id, ReviewRequest newRequest) {
        return null;
    }

    @Override
    public List<ReviewResponse> getAllReview(Long productId) {
        return null;
    }
}
