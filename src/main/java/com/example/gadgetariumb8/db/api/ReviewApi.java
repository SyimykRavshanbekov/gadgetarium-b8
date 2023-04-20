package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.request.ReviewRequest;
import com.example.gadgetariumb8.db.dto.response.ReviewResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * name : kutman
 **/
@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
@Tag(name = "Review API")
public class ReviewApi {
    private final ReviewService reviewService;

    @PostMapping("/save")
    @Operation(summary = "Save Review", description = "This method save Review")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public SimpleResponse saveReview(@RequestBody @Valid ReviewRequest request, @RequestParam Long userId,@RequestParam Long productId) {
        return reviewService.saveReview(request, userId, productId);
    }

    @GetMapping("/getById")
    @Operation(summary = "Get By id Review", description = "This method get by Id")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ReviewResponse getById(@RequestParam Long id) {
        return reviewService.getByIdReview(id);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete Review", description = "This method delete Review")
    @PreAuthorize("hasAuthority('ADMIN')")
    public SimpleResponse deleteById(@RequestParam Long id) {
        return reviewService.deleteById(id);
    }

    @PutMapping("/update")
    @Operation(summary = "Update Review", description = "This method update Review")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public SimpleResponse updateReview(@RequestBody @Valid ReviewRequest request,@RequestParam Long id) {
        return reviewService.updateReview(id,request);
    }

    @GetMapping("/getAll")
    @Operation(summary = "Get All Review", description = "This method get All")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public List<ReviewResponse> getAll(@RequestParam Long id) {
        return reviewService.getAllReview(id);
    }


    @PostMapping("/reply")
    @Operation(summary = "Get All Review", description = "FEEDBACK")
    @PreAuthorize("hasAuthority('ADMIN')")
    public SimpleResponse replyToFeedback(@RequestParam Long id,@RequestBody String answer) {
        return reviewService.replyToFeedback(answer, id);
    }

}
