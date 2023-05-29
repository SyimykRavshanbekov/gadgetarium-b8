package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.request.BannerRequest;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.model.Banner;

import java.util.List;

public interface BannerService {
    SimpleResponse saveBanners(BannerRequest request);
    SimpleResponse deleteBanners(Long id);
    List<Banner> getAllBanners();
}
