package com.example.gadgetariumb8.db.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface StorageService {
    Map<String, String> upload(MultipartFile file) throws IOException;

    Map<String, String> delete(String fileLink);
}