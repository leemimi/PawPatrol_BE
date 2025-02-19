package com.patrol.api.member.member.dto.request;



import com.patrol.domain.member.member.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MemberUpdateRequest {

    @NotBlank
    private String nickname;
    @NotNull
    private boolean marketingAgree;

    private Gender gender;
    private String address;
    private String phoneNumber;
    private LocalDate birthDate;
}
