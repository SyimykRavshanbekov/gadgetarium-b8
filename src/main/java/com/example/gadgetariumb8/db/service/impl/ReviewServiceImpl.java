package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.response.ReviewResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.exception.exceptions.BadCredentialException;
import com.example.gadgetariumb8.db.model.Review;
import com.example.gadgetariumb8.db.repository.ReviewRepository;
import com.example.gadgetariumb8.db.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    @Override
    public SimpleResponse deleteById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new BadCredentialException(String.format("Review with id %s does not exists", id)));
        reviewRepository.deleteById(review.getId());
        return SimpleResponse.builder().message(String.format("Review with id %s deleted", id)).httpStatus(HttpStatus.OK).build();
    }

    @Override
    public SimpleResponse replyToFeedback(String answer, Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new BadCredentialException(String.format("Review with id %s does not exists", id)));
        if (answer.length() > 300 || answer.length() < 2) {
            return SimpleResponse.builder().message("answer must be between 2 and 300 characters").httpStatus(HttpStatus.BAD_REQUEST).build();
        } else {
            review.setAnswer(answer);
            reviewRepository.save(review);
            return SimpleResponse.builder().message("answer successfully saved").httpStatus(HttpStatus.OK).build();
        }
    }

    @Override
    public List<ReviewResponse> getAllReview(String param) {
        List<ReviewResponse> getAll = reviewRepository.findAllResponse();
        for (ReviewResponse reviewResponse : getAll) {
            reviewResponse.setImages(reviewRepository.getAllImage(reviewResponse.getId()));
        }
        if (param.equals("Unanswered")) {
            return getAll.stream().filter(reviewResponse -> reviewResponse.getAnswer().isEmpty()).toList();
        } else if (param.equals("Answered")) {
            return getAll.stream().filter(reviewResponse -> !reviewResponse.getAnswer().isEmpty()).toList();
        } else if (param.equals("AllReviews")) {
            return getAll;
        } else {
            return null;
        }
    }
}
