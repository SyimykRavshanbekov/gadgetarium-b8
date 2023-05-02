package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.request.ProfileImageRequest;
import com.example.gadgetariumb8.db.dto.request.ProfileRequest;
import com.example.gadgetariumb8.db.dto.response.ProfileResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileApi {
    private final ProfileService profileService;
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('USER')")
    public ProfileResponse update(@RequestBody @Valid ProfileRequest request) {
        return profileService.update(request);
    }

    @PostMapping("/set-image")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public SimpleResponse setProfileImage(@RequestBody @Valid ProfileImageRequest request) {
        return profileService.setImage(request);
    }
}
