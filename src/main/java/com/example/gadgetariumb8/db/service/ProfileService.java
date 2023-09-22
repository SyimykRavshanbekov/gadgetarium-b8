package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.request.ProfileImageRequest;
import com.example.gadgetariumb8.db.dto.request.ProfilePasswordResetRequest;
import com.example.gadgetariumb8.db.dto.request.ProfileRequest;
import com.example.gadgetariumb8.db.dto.response.ProfileResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;

public interface ProfileService {
    ProfileResponse updateUserDetails(ProfileRequest request);

    SimpleResponse resetPassword(ProfilePasswordResetRequest request);

    SimpleResponse setImage(ProfileImageRequest request);

    ProfileResponse getProfile();
}

