package com.patrol.domain.findPost.entity;

import com.patrol.api.findPost.dto.FindPostRequestDto;
import com.patrol.domain.image.entity.Image;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class FindPost extends BaseEntity {

    private Member author;

    @OneToMany(mappedBy = "foundId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    private String title;
    private String content;
    private Double latitude;
    private Double longitude;
    private String location;
    private String findTime;


    @Enumerated(EnumType.STRING)
    private Status status;

    public FindPost(FindPostRequestDto requestDto, Member author) {
        this(requestDto);
        this.author = author;
    }

    // Enum 정의
    public enum Status {
        SIGHTED("목격"),
        FOSTERING("임보"),
        SHELTER("보호소");

        private final String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }


    // 생성자 (FindPostRequestDto로부터 값 초기화)
    public FindPost(FindPostRequestDto requestDto) {
        this.content = requestDto.getContent();
        this.latitude = requestDto.getLatitude();
        this.longitude = requestDto.getLongitude();
        this.location = requestDto.getLocation();
        this.findTime = requestDto.getFindTime();
        if (requestDto.getStatus() != null) {
            this.status = Status.valueOf(requestDto.getStatus());
        } else {
            this.status = Status.SIGHTED; // 기본값 설정
        }
    }


    public void addImage(Image image) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.add(image);
    }
}
