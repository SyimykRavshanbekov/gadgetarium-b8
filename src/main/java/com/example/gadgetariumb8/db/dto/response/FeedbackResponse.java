package com.example.gadgetariumb8.db.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class FeedbackResponse {
    private int five;
    private int four;
    private int three;
    private int two;
    private int one;
    private double rating;
    private int totalReviews;
}
