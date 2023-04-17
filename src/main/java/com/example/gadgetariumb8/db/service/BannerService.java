package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.request.BannerRequest;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;

public interface BannerService {
    SimpleResponse saveBanners(BannerRequest request);
}
