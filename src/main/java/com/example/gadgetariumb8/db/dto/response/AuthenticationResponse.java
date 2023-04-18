package com.example.gadgetariumb8.db.dto.response;

import com.example.gadgetariumb8.db.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
public record AuthenticationResponse(
        String email,
        Role role,
        String token
) {
}
