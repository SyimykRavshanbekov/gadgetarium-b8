package com.example.gadgetariumb8.db.dto.request;

import lombok.Builder;

@Builder
public record ResetPasswordRequest(
        String newPassword
){
}
