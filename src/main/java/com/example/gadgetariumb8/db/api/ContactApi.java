package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.request.ContactRequest;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
@Tag(name = "Contact API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ContactApi {
    private final ContactService service;

    @Operation(summary = "Save contact", description = "This method saves contact.")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public SimpleResponse save(@RequestBody @Valid ContactRequest request) {
        return service.save(request);
    }
}
