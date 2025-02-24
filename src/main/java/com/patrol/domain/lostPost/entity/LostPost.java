package com.patrol.domain.lostPost.entity;

import com.patrol.api.lostPost.dto.LostPostRequestDto;
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "lost_id")
    private List<Image> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    private Long petId;
    private Double latitude;
    private Double longitude;
    private String content;
    private String location;
    private LocalDateTime lostTime;

    public LostPost(LostPostRequestDto requestDto, Member author) {
        this.author= author;
        this.content = requestDto.getContent();
        this.longitude = requestDto.getLongitude();
        this.latitude = requestDto.getLatitude();
        this.location = requestDto.getLocation();
        this.lostTime = requestDto.getLostTime();
    }

    public void update(LostPostRequestDto requestDto) {
        this.content = requestDto.getContent();
        this.location = requestDto.getLocation();
        this.longitude = requestDto.getLongitude();
        this.latitude = requestDto.getLatitude();
        this.lostTime = requestDto.getLostTime();
    }

    public void addImage(Image image) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.add(image);
    }

}
