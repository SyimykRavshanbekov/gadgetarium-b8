package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.request.AuthenticateRequest;
import com.example.gadgetariumb8.db.dto.request.RegisterRequest;
import com.example.gadgetariumb8.db.dto.response.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticateRequest request);
}
