package com.patrol.domain.lostFoundPost.entity;

import com.patrol.api.lostFoundPost.dto.LostFoundPostRequestDto;
import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.image.entity.Image;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "lost_found_post")
public class LostFoundPost extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    @OneToMany
    @JoinColumn(name = "found_id")
    private List<Image> images = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "pet_id", nullable = true)
    private Animal pet;

    private String title;
    private String content;
    private Double latitude;
    private Double longitude;
    private String location;
    private String findTime;
    private String lostTime;
    @Enumerated(EnumType.STRING)
    private PostStatus status;

    public LostFoundPost(LostFoundPostRequestDto requestDto, Member author) {
        this(requestDto);
        this.author = author;
    }

    public LostFoundPost(LostFoundPostRequestDto requestDto, Member author, Animal pet) {
        this(requestDto);
        this.author = author;
        this.pet= pet;
    }


    // 생성자 (FindPostRequestDto로부터 값 초기화)
    public LostFoundPost(LostFoundPostRequestDto requestDto) {
        this.content = requestDto.getContent();
        this.latitude = requestDto.getLatitude();
        this.longitude = requestDto.getLongitude();
        this.location = requestDto.getLocation();
        this.findTime = requestDto.getFindTime();
        this.lostTime=requestDto.getLostTime();
        if (requestDto.getStatus() != null) {
            this.status = PostStatus.valueOf(requestDto.getStatus());
        } else {
            this.status = PostStatus.FINDING; // 기본값 설정
        }
    }


    public void addImage(Image image) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.add(image);
    }
}
