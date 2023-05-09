package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.BannerRequest;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.exception.exceptions.BadRequestException;
import com.example.gadgetariumb8.db.model.Banner;
import com.example.gadgetariumb8.db.repository.BannerRepository;
import com.example.gadgetariumb8.db.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {
    private final BannerRepository bannerRepository;

    @Override
    public SimpleResponse saveBanners(BannerRequest request) {
        for (String banner : request.getBannerList()) {
            if (request.getBannerList().size() < 6) {
                if (!banner.isBlank()) {
                    bannerRepository.save(new Banner(banner));
                } else {
                    throw new BadRequestException("One of the elements is empty!");
                }
            } else {
                throw new BadRequestException("Quantity of banners should not exceed 6!");
            }
        }
        return SimpleResponse.builder().message("The banner is well preserved!").httpStatus(HttpStatus.OK).build();
    }
}