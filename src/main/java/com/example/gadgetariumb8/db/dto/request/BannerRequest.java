package com.example.gadgetariumb8.db.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BannerRequest {

    @NotEmpty(message = "Should not be empty")
    @Size(min = 1, max = 6, message = "Should contain 1 to 6 elements")
    List<String> bannerList;
}
