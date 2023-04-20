package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.ReviewRequest;
import com.example.gadgetariumb8.db.dto.response.ReviewResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.exception.exceptions.BadCredentialException;
import com.example.gadgetariumb8.db.model.Product;
import com.example.gadgetariumb8.db.model.Review;
import com.example.gadgetariumb8.db.model.User;
import com.example.gadgetariumb8.db.repository.ProductRepository;
import com.example.gadgetariumb8.db.repository.ReviewRepository;
import com.example.gadgetariumb8.db.repository.UserRepository;
import com.example.gadgetariumb8.db.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * name : kutman
 **/
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    @Override
    public SimpleResponse saveReview(ReviewRequest newRequest, Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new BadCredentialException(String.format("User with id %s does not exists", userId)));
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new BadCredentialException(String.format("Product with id %s does not exists", productId)));
        if (newRequest.grade()>5||newRequest.grade()<0){
            return SimpleResponse.builder().message("you cannot rate more than 5 and less than 0").httpStatus(HttpStatus.BAD_REQUEST).build();
        }
        reviewRepository.save(new Review(newRequest.commentary(), newRequest.grade(),newRequest.images(),product,user));
        return SimpleResponse.builder().message("review successfully saved").httpStatus(HttpStatus.OK).build();
    }

    @Override
    public ReviewResponse getByIdReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(()-> new BadCredentialException(String.format("Review with id %s does not exists", id)));
        return ReviewResponse.builder()
                .id(review.getId())
                .productImg(review.getProduct().getPDF())
                .productItemNumber(review.getProduct().getItemNumber())
                .commentary(review.getCommentary())
                .grade(review.getGrade())
                .answer(review.getAnswer())
                .images(review.getImages())
                .userName(review.getUser().getFirstName().concat("  "+review.getUser().getLastName()))
                .userEmail(review.getUser().getUserInfo().getEmail())
                .userImg(review.getUser().getImage())
                .build();
    }

    @Override
    public SimpleResponse deleteById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(()-> new BadCredentialException(String.format("Review with id %s does not exists", id)));
        reviewRepository.deleteById(review.getId());
        return SimpleResponse.builder().message(String.format("Review with id %s deleted", id)).httpStatus(HttpStatus.OK).build();
    }

    @Override
    public SimpleResponse updateReview(Long id, ReviewRequest newRequest) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(()-> new BadCredentialException(String.format("Review with id %s does not exists", id)));
        if (!newRequest.commentary().isBlank()){
            review.setCommentary(newRequest.commentary());
        }
        if(newRequest.images().size()!=0){
            review.setImages(newRequest.images());
        }
        if (newRequest.grade()>5||newRequest.grade()<0){
            return SimpleResponse.builder().message("you cannot rate more than 5 and less than 0").httpStatus(HttpStatus.BAD_REQUEST).build();
        }
        review.setGrade(newRequest.grade());
        reviewRepository.save(review);
        return SimpleResponse.builder().message("update").httpStatus(HttpStatus.OK).build();
    }

    @Override
    public List<ReviewResponse> getAllReview(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new BadCredentialException(String.format("Product with id %s does not exists", productId)));
        List<ReviewResponse> getAll = reviewRepository.findAllResponse(productId);
        for (ReviewResponse reviewResponse : getAll) {
            reviewResponse.setImages(reviewRepository.getAllImage(reviewResponse.getId()));
        }
       return getAll;
    }

    @Override
    public SimpleResponse replyToFeedback(String answer, Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(()-> new BadCredentialException(String.format("Review with id %s does not exists", id)));
        if(answer.length()>300){
            return SimpleResponse.builder().message("answer must be between 2 and 500 characters").httpStatus(HttpStatus.BAD_REQUEST).build();
        }else {
            review.setAnswer(answer);
            reviewRepository.save(review);
            return SimpleResponse.builder().message("answer successfully saved").httpStatus(HttpStatus.OK).build();
        }
    }
}
