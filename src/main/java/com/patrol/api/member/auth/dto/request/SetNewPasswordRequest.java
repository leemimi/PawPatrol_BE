package com.patrol.api.member.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SetNewPasswordRequest(
    @NotBlank
    @Email
    String email,

    @NotBlank
    String newPassword
) {}
