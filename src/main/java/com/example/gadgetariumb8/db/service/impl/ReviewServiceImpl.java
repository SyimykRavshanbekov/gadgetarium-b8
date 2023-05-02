package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.AnswerRequest;
import com.example.gadgetariumb8.db.dto.response.ReviewResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.Review;
import com.example.gadgetariumb8.db.repository.ReviewRepository;
import com.example.gadgetariumb8.db.service.ReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public SimpleResponse deleteById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Review with id %s not found", id)));
        reviewRepository.deleteById(review.getId());
        return SimpleResponse.builder().message(String.format("Review with id %s deleted", id)).httpStatus(HttpStatus.OK).build();
    }

    @Override
    public SimpleResponse replyToFeedback(AnswerRequest answerRequest) {
        Review review = reviewRepository.findById(answerRequest.reviewId())
                .orElseThrow(() -> new NotFoundException(String.format("Review with id %s does not exists", answerRequest.reviewId())));
        if (!review.getAnswer().isEmpty()) {
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(String.format("Review with id - %s has already been answered", answerRequest.reviewId()))
                    .build();
        }
        review.setAnswer(answerRequest.answer());
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("answer successfully saved")
                .build();
    }

    @Override
    public List<ReviewResponse> getAllReview(String param) {
        List<ReviewResponse> getAll = reviewRepository.findAllResponse();
        for (ReviewResponse reviewResponse : getAll) {
            reviewResponse.setImages(reviewRepository.getAllImage(reviewResponse.getId()));
        }
        return switch (param) {
            case "Unanswered" ->
                    getAll.stream().filter(reviewResponse -> reviewResponse.getAnswer().isEmpty()).toList();
            case "Answered" -> getAll.stream().filter(reviewResponse -> !reviewResponse.getAnswer().isEmpty()).toList();
            case "AllReviews" -> getAll;
            default -> null;
        };
    }
}
