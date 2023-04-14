package com.example.gadgetariumb8.db.services;

import com.example.gadgetariumb8.db.dto.request.BannerRequest;
import com.example.gadgetariumb8.db.dto.response.BannerResponse;
import jdk.dynalink.linker.LinkerServices;

import java.util.List;

/**
 * name : kutman
 **/
public interface BannerServices {
    List<BannerResponse> saveBanners(BannerRequest request);
}
