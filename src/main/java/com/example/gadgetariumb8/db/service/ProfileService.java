package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.request.ProfileImageRequest;
import com.example.gadgetariumb8.db.dto.request.ProfileRequest;
import com.example.gadgetariumb8.db.dto.response.ProfileResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;

public interface ProfileService {
    ProfileResponse update(ProfileRequest request);
    SimpleResponse setImage(ProfileImageRequest request);
}

