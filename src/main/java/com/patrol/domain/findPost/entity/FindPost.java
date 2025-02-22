package com.patrol.domain.findPost.entity;

import com.patrol.api.findPost.dto.FindPostRequestDto;
import com.patrol.domain.LostPost.entity.LostPost;
import com.patrol.domain.image.entity.Image;
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

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lostId", nullable = true) // 연관된 실종 게시글 (null 허용)
    private LostPost lostPost; // FindPost와 LostPost 간의 관계 (Long에서 LostPost로 변경)

    @OneToMany(mappedBy = "foundId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    private Long petId;
    private String title;
    private String content;
    private Double latitude;
    private Double longitude;
    private String location;
    private String findTime;
    private String tags;
    private boolean isRescue;

    @Enumerated(EnumType.STRING)
    private Status status;

    // 실종 게시글 ID (연계용)
    //private Long lostPostId; // 새로운 필드
    // 추가된 필드
    private LocalDate birthDate;  // 출생일

    private String breed;  // 품종
    private String name;  // 이름
    private String characteristics;  // 특징
    @Enumerated(EnumType.STRING)
    private Size size;  // 크기 (소형, 중형, 대형)
    @Enumerated(EnumType.STRING)
    private Gender gender;  // 성별 (남자, 여자)

    public FindPost(FindPostRequestDto requestDto, LostPost lostPost, Long memberId) {
        this(requestDto);
        this.lostPost = lostPost;
        this.memberId = memberId;
    }

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

    // Enum 정의 (크기)
    public enum Size {
        SMALL("소형"),
        MEDIUM("중형"),
        LARGE("대형");

        private final String description;

        Size(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    // Enum 정의 (성별)
    public enum Gender {
        MALE("남자"),
        FEMALE("여자");

        private final String description;

        Gender(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }


    // 생성자 (FindPostRequestDto로부터 값 초기화)
    public FindPost(FindPostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.latitude = requestDto.getLatitude();
        this.longitude = requestDto.getLongitude();
        this.location = requestDto.getLocation();
        this.findTime = requestDto.getFindTime();
        this.tags = String.join("#", requestDto.getTags());
        if (requestDto.getStatus() != null) {
            this.status = Status.valueOf(requestDto.getStatus());
        } else {
            this.status = Status.FINDING; // 기본값 설정
        }
        // 새로운 필드들 초기화
        this.birthDate = requestDto.getBirthDate();
        this.breed = requestDto.getBreed();
        this.name = requestDto.getName();
        this.characteristics = requestDto.getCharacteristics();
        if (requestDto.getSize() != null) {
            this.size = Size.valueOf(requestDto.getSize());
        } else {
            this.size = Size.SMALL; // 기본값 설정
        }
        if (requestDto.getGender() != null) {
            this.gender = Gender.valueOf(requestDto.getGender());
        } else {
            this.gender = Gender.MALE; // 기본값 설정
        }

    }
    // 신고글 ID를 반환하는 getter 추가
    public Long getLostId() {
        return (lostPost != null) ? lostPost.getLostId() : null;
    }

    // 새로운 생성자 (연계없는 제보글 생성 시)
    public FindPost(FindPostRequestDto requestDto, LostPost lostPost) {
        this(requestDto);  // 기존 생성자를 호출하여 기본 값 설정
        this.lostPost = lostPost;  // 연계된 실종 게시글 객체를 추가 설정
    }

    public void addImage(Image image) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.add(image);
    }
}
