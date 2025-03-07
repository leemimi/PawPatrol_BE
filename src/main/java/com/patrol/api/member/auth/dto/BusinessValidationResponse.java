package com.patrol.api.member.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * packageName    : com.patrol.api.member.auth.dto
 * fileName       : BusinessValidationResponse
 * author         : sungjun
 * date           : 2025-03-04
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-04        kyd54       최초 생성
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessValidationResponse {
    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("data")
    private List<ValidationResult> data;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ValidationResult {
        @JsonProperty("valid")
        private String valid;
    }

    // valid 값만 추출하는 편의 메소드
    public String getValidStatus() {
        if (data != null && !data.isEmpty() && data.get(0) != null) {
            return data.get(0).getValid();
        }
        return null;
    }

    // 유효한 사업자등록번호인지 확인하는 편의 메소드
    public boolean isValid() {
        String validStatus = getValidStatus();
        return "01".equals(validStatus);
    }
}
