package com.example.gadgetariumb8.db.services.impl;

import com.example.gadgetariumb8.db.dto.request.BannerRequest;
import com.example.gadgetariumb8.db.dto.response.BannerResponse;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.Banner;
import com.example.gadgetariumb8.db.repository.BannerRepository;
import com.example.gadgetariumb8.db.services.BannerServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * name : kutman
 **/
@Service
@RequiredArgsConstructor
public class BannerServicesImpl implements BannerServices {
    private final BannerRepository bannerRepository;

    @Override
    public List<BannerResponse> saveBanners(BannerRequest request) {
        List<BannerResponse> bannerResponse = new ArrayList<>();
        if (request.getBannerList().size() != 0) {
            for (String banner : request.getBannerList()) {
                if (!banner.isBlank()) {
                    Banner banner1 = new Banner(banner);
                    bannerRepository.save(banner1);
                    bannerResponse.add(new BannerResponse(banner1.getId(), banner1.getBanner()));
                } else {
                    throw new NotFoundException("One of the elements is empty!");
                }
            }
            return bannerResponse;
        }else {
            throw new NotFoundException("One of the elements is empty!");
        }
    }
}
