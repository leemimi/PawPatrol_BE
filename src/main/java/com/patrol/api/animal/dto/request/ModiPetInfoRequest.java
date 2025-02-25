package com.patrol.api.animal.dto.request;

import com.patrol.domain.animal.enums.AnimalSize;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * packageName    : com.patrol.api.animal.dto.request
 * fileName       : ModiPetInfoRequest
 * author         : sungjun
 * date           : 2025-02-25
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-25        kyd54       최초 생성
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModiPetInfoRequest {
    private Long id;
    private String estimatedAge;
    private String feature;
    @Enumerated(EnumType.STRING)
    private AnimalSize size;
    private String registrationNo;
    private MultipartFile imageFile;
    private String healthCondition;
}
