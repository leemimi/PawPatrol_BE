package com.patrol.api.lostFoundPost.dto;

import com.patrol.api.animal.dto.PetResponseDto;
import com.patrol.api.image.dto.ImageResponseDto;
import com.patrol.api.member.member.dto.MemberResponseDto;
import com.patrol.domain.lostFoundPost.entity.LostFoundPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LostFoundPostResponseDto {
    private Long foundId;
    private MemberResponseDto author;
    private Long userId;  // ✅ 추가된 필드 (작성자의 ID)
    private String nickname;  // ✅ 추가된 필드 (작성자 닉네임)
    private String content;
    private Double latitude;
    private Double longitude;
    private String findTime;
    private String location;
    private String lostTime;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String status;
    private PetResponseDto pet;
    private List<ImageResponseDto> images;
    private String animalType;
    //private Long petId;  // petId 추가
    // Add petId to the ResponseDto
    //private Long petId;

    public LostFoundPostResponseDto(LostFoundPost lostFoundPost) {
        this.foundId = lostFoundPost.getId();
        this.author = new MemberResponseDto(lostFoundPost.getAuthor());
        this.userId = lostFoundPost.getAuthor().getId();  // ✅ 유저 ID 추가
        this.nickname = lostFoundPost.getAuthor().getNickname();  // ✅ Member에서 nickname 가져오기
        this.content = lostFoundPost.getContent();
        this.latitude = lostFoundPost.getLatitude();
        this.longitude = lostFoundPost.getLongitude();
        this.findTime = lostFoundPost.getFindTime();
        this.lostTime = lostFoundPost.getLostTime();
        // Set animalType based on pet or lostFoundPost's animalType
        if (lostFoundPost.getPet() != null && lostFoundPost.getPet().getAnimalType() != null) {
            // Convert AnimalType enum to String
            this.animalType = lostFoundPost.getPet().getAnimalType().toString(); // Convert enum to String
        } else {
            // If animalType in pet is null, set it from lostFoundPost
            this.animalType = (lostFoundPost.getAnimalType() != null && !lostFoundPost.getAnimalType().toString().equals("null"))
                    ? lostFoundPost.getAnimalType().toString() // Convert to String
                    : "null"; // If it is "null", explicitly set it to "null" // If animalType is "null" set it to "null" explicitly
        }
        this.status= String.valueOf(lostFoundPost.getStatus());
        this.location = lostFoundPost.getLocation();
        this.createdAt = lostFoundPost.getCreatedAt();
        this.modifiedAt = lostFoundPost.getModifiedAt();
        this.pet = lostFoundPost.getPet() != null ? new PetResponseDto(lostFoundPost.getPet()) : null;

        this.images = lostFoundPost.getImages().stream()
                .map(ImageResponseDto::new)
                .collect(Collectors.toList());


    }

    public static LostFoundPostResponseDto from(LostFoundPost lostFoundPost) {
        return new LostFoundPostResponseDto(lostFoundPost);
    }




}

