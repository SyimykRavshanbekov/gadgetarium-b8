package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.request.ForgotPasswordRequest;
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
@CrossOrigin(origins = "*", maxAge = 3600)
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

    @Operation(summary = "Forgot password", description = "This method sends message to email for reset password.")
    @PostMapping("/forgot-password")
    public ResponseEntity<SimpleResponse> processForgotPasswordForm(@RequestBody @Valid ForgotPasswordRequest request) throws MessagingException {
        return ResponseEntity.ok(authenticationService.
                forgotPassword(request));
    }

    @Operation(summary = "Reset password", description = "This method changes the old password to new password.")
    @PostMapping("/reset-password")
    public ResponseEntity<SimpleResponse> resetPassword(@RequestParam String token, @RequestBody @Valid ResetPasswordRequest request) {
        return ResponseEntity.ok(authenticationService.resetPassword(token, request.newPassword()));
    }
}
