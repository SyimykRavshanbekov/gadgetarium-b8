package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.service.AuthenticationService;
import com.example.gadgetariumb8.db.dto.request.AuthenticateRequest;
import com.example.gadgetariumb8.db.dto.request.RegisterRequest;
import com.example.gadgetariumb8.db.dto.response.AuthenticationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kurstan
 * @created at 13.04.2023 10:02
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationApi {

    private final AuthenticationService authenticationService;

    @PostMapping("/signUp")
    public AuthenticationResponse signUp(@RequestBody  @Valid RegisterRequest request){
        return authenticationService.register(request);
    }

    @PostMapping("/signIn")
    public AuthenticationResponse signIn(@RequestBody @Valid AuthenticateRequest request){
        return authenticationService.authenticate(request);
    }
}
