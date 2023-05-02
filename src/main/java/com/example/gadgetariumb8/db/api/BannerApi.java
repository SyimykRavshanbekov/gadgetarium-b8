package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.request.BannerRequest;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/banners")
@RequiredArgsConstructor
@Tag(name = "Banner API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BannerApi {
    private final BannerService bannerServices;

    @PostMapping
    @Operation(summary = "Save banner", description = "This method save banners")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public SimpleResponse saveBanner(@RequestBody @Valid BannerRequest request) {
        return bannerServices.saveBanners(request);
    }
}
