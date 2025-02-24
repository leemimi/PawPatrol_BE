package com.patrol.domain.lostPost.entity;

import com.patrol.api.lostpost.dto.LostPostRequestDto;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
    private String content;
    private String location;
    private LocalDateTime lostTime;

    public LostPost(LostPostRequestDto requestDto, Member author) {
        this.author= author;
        this.content = requestDto.getContent();
        this.longitude = requestDto.getLongitude();
        this.latitude = requestDto.getLatitude();
        this.location = requestDto.getLocation();
        this.lostTime = LocalDateTime.parse(requestDto.getLostTime());
    }

    public void update(LostPostRequestDto requestDto) {
        this.content = requestDto.getContent();
        this.location = requestDto.getLocation();
        this.longitude = requestDto.getLongitude();
        this.latitude = requestDto.getLatitude();
        this.lostTime = LocalDateTime.parse(requestDto.getLostTime());
    }

}
