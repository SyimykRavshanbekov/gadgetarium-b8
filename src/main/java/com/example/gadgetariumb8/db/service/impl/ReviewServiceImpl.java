package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.AnswerRequest;
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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewsRepository reviewsRepository;

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
        if (review.getAnswer()==null){
            review.setAnswer(answerRequest.answer());
            reviewRepository.save(review);
            log.info("answer successfully saved");
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("answer successfully saved")
                    .build();
        }else {
            log.info(String.format("Review with id - %s has already been answered", answerRequest.reviewId()));
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(String.format("Review with id - %s has already been answered", answerRequest.reviewId()))
                    .build();
        }
    }

    @Override
    public List<ReviewResponse> getAllReview(String param) {
        log.info("Getting all reviews");
        List<ReviewResponse> getAll = reviewRepository.findAllResponse();
        for (ReviewResponse reviewResponse : getAll) {
            reviewResponse.setImages(reviewRepository.getAllImage(reviewResponse.getId()));
        }
        log.info("All reviews are got!");
        return switch (param) {
            case "Unanswered" ->
                    getAll.stream().filter(reviewResponse -> reviewResponse.getAnswer().isEmpty()).toList();
            case "Answered" -> getAll.stream().filter(reviewResponse -> !reviewResponse.getAnswer().isEmpty()).toList();
            case "AllReviews" -> getAll;
            default -> null;
        };
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
            }else {
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
