package com.patrol.domain.lostpost.entity;

import com.patrol.api.findPost.dto.FindPostRequestDto;
import com.patrol.api.lostpost.dto.LostPostRequestDto;
import com.patrol.domain.image.entity.Image;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LostPost extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;
    private Long petId;
    private Double latitude;
    private Double longitude;
    private String title;
    private String content;
    private String location;
    private String ownerPhone;
    @OneToMany(mappedBy = "lostId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Status status; // 상태를 Enum으로 관리

    private String tags;
    private String  lostTime;


    // Enum 정의
    public enum Status {
        FINDING("찾는 중"),
        FOSTERING("임보 중"),
        FOUND("주인 찾기 완료");

        private final String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public LostPost(LostPostRequestDto requestDto,Member author) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.location = requestDto.getLocation();
        this.ownerPhone = requestDto.getOwnerPhone();
        this.tags = String.join("#", requestDto.getTags());
        this.lostTime = requestDto.getLostTime();
        this.author = author; // 작성자 설정

        // 상태를 Enum으로 변환하여 저장
        if (requestDto.getStatus() != null) {
            this.status = Status.valueOf(requestDto.getStatus()); // status를 enum으로 변환
        } else {
            this.status = Status.FINDING; // 기본값으로 "찾는 중"을 설정
        }
    }

    public void update(LostPostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.location = requestDto.getLocation();
        this.ownerPhone = requestDto.getOwnerPhone();
        this.tags = String.join("#", requestDto.getTags());
        this.lostTime = requestDto.getLostTime();

        // 상태를 Enum으로 변환하여 업데이트
        if (requestDto.getStatus() != null) {
            this.status = Status.valueOf(requestDto.getStatus()); // status를 enum으로 변환
        }
    }





    public void addImage(Image image) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.add(image);
    }

    // ID만을 이용한 생성자 추가
    /*public LostPost(Long lostId) {
        this.lostId = lostId;
    }*/
}
