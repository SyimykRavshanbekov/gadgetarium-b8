package com.example.gadgetariumb8.db.dto.response;

import lombok.*;

import java.util.List;

/**
 * name : kutman
 **/

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ReviewResponse {
    private Long id;
    private String productImg;
    private String productItemNumber;
    private String commentary;
    private int grade;
    private String answer;
    private List<String> images;
    private String userName;
    private String userEmail;
    private String userImg;

    public ReviewResponse(Long id, String productImg, String productItemNumber, String commentary, int grade, String answer, String userName, String userEmail, String userImg) {
        this.id = id;
        this.productImg = productImg;
        this.productItemNumber = productItemNumber;
        this.commentary = commentary;
        this.grade = grade;
        this.answer = answer;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userImg = userImg;
    }
}
