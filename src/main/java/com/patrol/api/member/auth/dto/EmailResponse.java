package com.patrol.api.member.auth.dto;

import java.time.LocalDateTime;

public record EmailResponse(
    String maskedEmail,
    LocalDateTime createdAt  // 가입일
) {}
