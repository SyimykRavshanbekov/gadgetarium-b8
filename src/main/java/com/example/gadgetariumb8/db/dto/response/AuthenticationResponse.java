package com.example.gadgetariumb8.db.dto.response;

import com.example.gadgetariumb8.db.model.enums.Role;
import lombok.Builder;

/**
 * @author kurstan
 * @created at 13.04.2023 10:05
 */
@Builder
public record AuthenticationResponse(
        String email,
        Role role,
        String token
) {
}
