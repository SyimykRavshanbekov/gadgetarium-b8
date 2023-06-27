package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.request.AnswerRequest;
import com.example.gadgetariumb8.db.dto.response.AdminReviewsResponse;
import com.example.gadgetariumb8.db.dto.response.FeedbackInfographic;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Admin Reviews API")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminReviewApi {
    private final ReviewService reviewService;

    @GetMapping
    @Operation(summary = "All Review", description = "This method is needed to display all review. " +
                                                     " The request param must accept the word -(AllReviews, Answered, Unanswered)")
    public AdminReviewsResponse getAll(@RequestParam String param) {
        return reviewService.getAllReview(param);
    }

    @PostMapping
    @Operation(summary = "Reply to comment", description = "This method is needed to reply to a review")
    public SimpleResponse replyToFeedback(@RequestBody @Valid AnswerRequest request) {
        return reviewService.replyToFeedback(request);
    }

    @DeleteMapping
    @Operation(summary = "Delete Review", description = "This method is needed to remove a review")
    public SimpleResponse deleteById(@RequestParam Long id) {
        return reviewService.deleteById(id);
    }

    @PutMapping
    @Operation(summary = "Update Feedback", description = "This method updates the comment response")
    public SimpleResponse updateFeedback(@RequestBody @Valid AnswerRequest request){
        return reviewService.updateFeedback(request.reviewId(), request.answer());
    }

    @GetMapping("/feedback-infographic")
    @Operation(summary = "Feedback infographic", description = "This method to get infographic of all feedbacks")
    public FeedbackInfographic getFeedbackInfographic(){
        return reviewService.getFeedbackInfographic();
    }
}
