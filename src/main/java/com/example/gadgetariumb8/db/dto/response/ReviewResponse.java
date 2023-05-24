package com.example.gadgetariumb8.db.dto.response;

import lombok.*;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ReviewResponse {
    private Long id;
    private String productImg;
    private int productItemNumber;
    private String commentary;
    private int grade;
    private String answer;
    private List<String> images;
    private String userName;
    private String userEmail;
    private String userImg;
    private String date;
    private String productName;
}
