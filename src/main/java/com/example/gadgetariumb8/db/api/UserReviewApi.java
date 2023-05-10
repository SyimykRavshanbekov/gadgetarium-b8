package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.response.FeedbackResponse;
import com.example.gadgetariumb8.db.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/reviews")
@RequiredArgsConstructor
@Tag(name = "User Reviews API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserReviewApi {
    private final ReviewService reviewService;
    @Operation(summary = "Get feedback!", description = "This method gets product's feedback!")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{productId}")
    public FeedbackResponse getFeedback(@PathVariable Long productId){
        return reviewService.getFeedbacks(productId);
    }
}
