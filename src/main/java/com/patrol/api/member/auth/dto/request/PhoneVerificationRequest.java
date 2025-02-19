package com.patrol.api.member.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PhoneVerificationRequest(
    @NotNull
    @Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$")
    String phoneNumber,

    @NotNull
    @Pattern(regexp = "^\\d{6}$")
    String code
) {}
