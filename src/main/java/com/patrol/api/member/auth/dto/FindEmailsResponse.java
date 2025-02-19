package com.patrol.api.member.auth.dto;

import java.util.List;

public record FindEmailsResponse(
    List<EmailResponse> emailResponses
) {}
