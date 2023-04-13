package com.example.gadgetariumb8.api;

import com.example.gadgetariumb8.db.service.AuthenticationService;
import com.example.gadgetariumb8.dto.request.AuthenticateRequest;
import com.example.gadgetariumb8.dto.request.RegisterRequest;
import com.example.gadgetariumb8.dto.response.AuthenticationResponse;
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
public class AuthenticationApi {

    private final AuthenticationService authenticationService;

    public AuthenticationApi(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signUp")
    public AuthenticationResponse signUp(@RequestBody  RegisterRequest request){
        return authenticationService.register(request);
    }

    @PostMapping("/signIn")
    public AuthenticationResponse signIn(@RequestBody AuthenticateRequest request){
        return authenticationService.authenticate(request);
    }
}
