package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.BannerRequest;
import com.example.gadgetariumb8.db.dto.response.BannerResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.Banner;
import com.example.gadgetariumb8.db.repository.BannerRepository;
import com.example.gadgetariumb8.db.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {
    private final BannerRepository bannerRepository;

    @Override
    public SimpleResponse saveBanners(BannerRequest request) {
        List<BannerResponse> bannerResponse = new ArrayList<>();
        if (request.getBannerList().size() != 0) {
            for (String banner : request.getBannerList()) {
                if (!banner.isBlank()) {
                    Banner banner1 = new Banner(banner);
                    bannerRepository.save(banner1);
                } else {
                    throw new NotFoundException("One of the elements is empty!");
                }
            }
            return new SimpleResponse("The banner is well preserved!", HttpStatus.OK);
        }else {
            throw new NotFoundException("One of the elements is empty!");
        }
    }
}
