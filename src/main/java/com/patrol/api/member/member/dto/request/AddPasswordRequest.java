package com.patrol.api.member.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddPasswordRequest(
    @NotBlank String password
) {}
