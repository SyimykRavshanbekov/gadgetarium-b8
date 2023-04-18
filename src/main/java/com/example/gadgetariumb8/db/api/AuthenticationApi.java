package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.request.ResetPasswordRequest;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.service.AuthenticationService;
import com.example.gadgetariumb8.db.dto.request.AuthenticateRequest;
import com.example.gadgetariumb8.db.dto.request.RegisterRequest;
import com.example.gadgetariumb8.db.dto.response.AuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication API")
public class AuthenticationApi {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Register a new user", description = "This method validates the request and creates a new user.")
    @PostMapping("/sign-up")
    public AuthenticationResponse signUp(@RequestBody @Valid RegisterRequest request) {
        return authenticationService.register(request);
    }

    @Operation(summary = "Authenticate a user", description = "This method validates the request and authenticates a user.")
    @PostMapping("/sign-in")
    public AuthenticationResponse signIn(@RequestBody @Valid AuthenticateRequest request) {
        return authenticationService.authenticate(request);
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<SimpleResponse> processForgotPasswordForm(@RequestParam String email) throws MessagingException {
        return ResponseEntity.ok(authenticationService.
                forgotPassword(email));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<SimpleResponse> resetPassword(@RequestParam String token, @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(authenticationService.resetPassword(token, request.newPassword()));
    }

}
