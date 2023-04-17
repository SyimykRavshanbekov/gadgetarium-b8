package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.request.BannerRequest;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.service.BannerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/banners")
@RequiredArgsConstructor
public class BannerApi {
    private final BannerService bannerServices;

    @PostMapping()
    @Tag(name = "save banners")
    public SimpleResponse saveBanner(@RequestBody BannerRequest request) {
        return bannerServices.saveBanners(request);
    }

}
