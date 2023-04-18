package com.example.gadgetariumb8.db.dto.response;

import java.util.List;

/**
 * name : kutman
 **/
public record ReviewResponse(
        Long id,
        String productImg,
        int productItemNumber,
        String commentary,
        int grade,
        String answer,
        List<String>images,
        String userName,
        String userEmail,
        String userImg
) {
}
