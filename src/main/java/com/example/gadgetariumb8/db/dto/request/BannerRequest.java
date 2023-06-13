package com.example.gadgetariumb8.db.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BannerRequest {

    @NotEmpty(message = "Не должно быть пустым")
    @Size(min = 1, max = 6, message = "Должен содержать от 1 до 6 элементов")
    List<String> bannerList;
}
