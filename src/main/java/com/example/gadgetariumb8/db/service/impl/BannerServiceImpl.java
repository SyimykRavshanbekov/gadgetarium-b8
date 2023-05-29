package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.BannerRequest;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.exception.exceptions.BadRequestException;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.Banner;
import com.example.gadgetariumb8.db.repository.BannerRepository;
import com.example.gadgetariumb8.db.service.BannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BannerServiceImpl implements BannerService {
    private final BannerRepository bannerRepository;

    @Override
    public SimpleResponse saveBanners(BannerRequest request) {
        log.info("Saving banner!");
        for (String banner : request.getBannerList()) {
            if (request.getBannerList().size() < 7) {
                if (!banner.isBlank()) {
                    bannerRepository.save(new Banner(banner));
                } else {
                    log.error("One of the elements is empty!");
                    throw new BadRequestException("One of the elements is empty!");
                }
            } else {
                log.error("Quantity of banners should not exceed 6!");
                throw new BadRequestException("Quantity of banners should not exceed 6!");
            }
        }
        log.info("The banner is well preserved!");
        return SimpleResponse.builder().message("The banner is well preserved!").httpStatus(HttpStatus.OK).build();
    }

    @Override
    public SimpleResponse deleteBanners(Long id) {
        bannerRepository.delete(bannerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id:" + id + " not found!!!")));
        return SimpleResponse.builder()
                .message("Banner successfully deleted").httpStatus(HttpStatus.OK).build();
    }

    @Override
    public List<Banner> getAllBanners() {
        return bannerRepository.findAll();
    }
}