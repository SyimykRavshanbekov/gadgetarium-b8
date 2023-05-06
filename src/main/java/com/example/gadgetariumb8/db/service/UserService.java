package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.response.UserChosenOneResponse;

import java.util.List;

public interface UserService {
    List<UserChosenOneResponse> getAll();
}
