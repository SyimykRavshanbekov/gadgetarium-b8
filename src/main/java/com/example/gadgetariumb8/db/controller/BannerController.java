package com.example.gadgetariumb8.db.controller;

import com.example.gadgetariumb8.db.dto.request.BannerRequest;
import com.example.gadgetariumb8.db.dto.response.BannerResponse;
import com.example.gadgetariumb8.db.services.BannerServices;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * name : kutman
 **/
@RestController
@RequestMapping("/api/banners")
@RequiredArgsConstructor
public class BannerController {
    private final BannerServices bannerServices;

    @PostMapping("/saveBanner")
    public List<BannerResponse> saveBanner(@RequestBody BannerRequest request){
        return bannerServices.saveBanners(request);
    }

}
