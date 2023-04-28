package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.request.ContactRequest;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;

public interface ContactService {
    SimpleResponse save(ContactRequest request);
}
