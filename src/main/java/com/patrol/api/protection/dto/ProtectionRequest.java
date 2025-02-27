package com.patrol.api.protection.dto;


import com.patrol.domain.protection.enums.ProtectionType;

public record ProtectionRequest(
    String reason,
    ProtectionType protectionType
) {
}
