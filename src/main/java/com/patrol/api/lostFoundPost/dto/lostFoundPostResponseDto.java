package com.patrol.api.lostFoundPost.dto;

import com.patrol.api.animal.dto.PetResponseDto;
import com.patrol.api.image.dto.ImageResponseDto;
import com.patrol.api.member.member.dto.MemberResponseDto;
import com.patrol.api.member.member.dto.request.PetRegisterRequest;
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
public class lostFoundPostResponseDto {
    private Long id;
    private MemberResponseDto author;
    private String content;
    private Double latitude;
    private Double longitude;
    private String findTime;
    private String lostTime;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String status;
    private PetResponseDto pet;
    private List<ImageResponseDto> images;
    private String location;

    public lostFoundPostResponseDto(LostFoundPost lostFoundPost) {
        this.id = lostFoundPost.getId();
        this.author = new MemberResponseDto(lostFoundPost.getAuthor());
        this.content = lostFoundPost.getContent();
        this.latitude = lostFoundPost.getLatitude();
        this.longitude = lostFoundPost.getLongitude();
        this.location= lostFoundPost.getLocation();
        this.findTime = lostFoundPost.getFindTime();
        this.lostTime = lostFoundPost.getLostTime();
        this.status= String.valueOf(lostFoundPost.getStatus());
        this.createdAt = lostFoundPost.getCreatedAt();
        this.modifiedAt = lostFoundPost.getModifiedAt();
        this.pet = lostFoundPost.getPet() != null ? new PetResponseDto(lostFoundPost.getPet()) : null;
        this.images = lostFoundPost.getImages().stream()
                .map(ImageResponseDto::new)
                .collect(Collectors.toList());
    }

    public static lostFoundPostResponseDto from(LostFoundPost lostFoundPost) {
        return new lostFoundPostResponseDto(lostFoundPost);
    }
}

