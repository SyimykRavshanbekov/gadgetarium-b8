package com.example.gadgetariumb8.db.dto.response;

import com.example.gadgetariumb8.db.model.enums.Role;
import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String email,
        Role role,
        String token
) {
}
