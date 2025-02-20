package com.patrol.api.LostFound.dto;

import com.patrol.domain.LostFound.entity.FindPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindPostResponseDto {
    private Long foundId;
    private Long memberId;
    //private Long lostId;  // lostPost가 없을 경우 null 가능
    //private Long petId;
    private String title;
    private String content;
    private Double latitude;
    private Double longitude;
    private String findTime;
    private String tags;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // 추가된 필드들
    private LocalDate birthDate;         // 출생일
    private String breed;                // 품종
    private String name;                 // 이름
    private String characteristics;      // 특징
    private FindPost.Size size;                   // 크기
    private FindPost.Gender gender;               // 성별

    // 엔티티에서 DTO 변환 생성자
    public FindPostResponseDto(FindPost findPost) {
        this.foundId = findPost.getFoundId();
        this.memberId = findPost.getMemberId();
        //this.lostId = (findPost.getLostPost() != null) ? findPost.getLostPost().getLostId() : null; // 수정된 부분
        //this.lostId = findPost.getLostId();
        //this.petId = findPost.getPetId();
        this.title = findPost.getTitle();
        this.content = findPost.getContent();
        this.latitude = findPost.getLatitude();
        this.longitude = findPost.getLongitude();
        this.findTime = findPost.getFindTime();
        this.tags = findPost.getTags();
        this.createdAt = findPost.getCreatedAt();
        this.modifiedAt = findPost.getModifiedAt();

        // 추가된 필드들 세팅
        this.birthDate = findPost.getBirthDate();       // 출생일
        this.breed = findPost.getBreed();               // 품종
        this.name = findPost.getName();                 // 이름
        this.characteristics = findPost.getCharacteristics();  // 특징
        this.size = findPost.getSize();                 // 크기
        this.gender = findPost.getGender();             // 성별
    }
}

