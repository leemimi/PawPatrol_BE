package com.patrol.domain.lostFoundPost.entity;

import com.patrol.api.lostFoundPost.dto.LostFoundPostRequestDto;
import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animal.enums.AnimalType;
import com.patrol.domain.comment.entity.Comment;
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

    @OneToMany(mappedBy = "foundId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();


    @OneToMany(mappedBy = "lostFoundPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();  // Comments relationship

    @OneToOne(fetch = FetchType.EAGER)  // 즉시 로딩
    @JoinColumn(name = "pet_id", nullable = true)
    private Animal pet;

    @Enumerated(EnumType.STRING)
    private AnimalType animalType;  // Add animalType field


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

    public LostFoundPost(LostFoundPostRequestDto requestDto, Member author, Animal pet,AnimalType animalType) {
        this(requestDto);
        this.author = author;
        this.pet = pet;
        this.animalType = animalType;
    }


    // 생성자 (FindPostRequestDto로부터 값 초기화)
    public LostFoundPost(LostFoundPostRequestDto requestDto) {
        this.content = requestDto.getContent();
        this.latitude = requestDto.getLatitude();
        this.longitude = requestDto.getLongitude();
        this.location = requestDto.getLocation();
        // Check if lostTime is provided
        // Handle findTime and lostTime based on input
        if (requestDto.getLostTime() != null && requestDto.getFindTime() == null) {
            this.lostTime = requestDto.getLostTime();
            this.findTime = null;  // Ensure findTime is null when lostTime is provided
        } else if (requestDto.getFindTime() != null && requestDto.getLostTime() == null) {
            this.findTime = requestDto.getFindTime();
            this.lostTime = null;  // Ensure lostTime is null when findTime is provided
        } else {
            // Optionally, you could throw an error if both times are provided, but for now, set both to null
            this.findTime = null;
            this.lostTime = null;
        }
        // status 필드가 null이 아니면 PostStatus로 변환
        if (requestDto.getStatus() != null) {
            this.status = PostStatus.valueOf(requestDto.getStatus());
        } else {
            this.status = PostStatus.FINDING; // 기본값 설정
        }

        this.animalType = requestDto.getAnimalType() != null ? AnimalType.valueOf(requestDto.getAnimalType()) : null;  // Set animalType

    }

    public LostFoundPost (LostFoundPostRequestDto requestDto, Member author, Animal pet) {
        this(requestDto, author);
        this.pet = pet;
    }

    public void addImage(Image image) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.add(image);
    }

    public void addComment (String comment) {
        Comment newComment = new Comment(comment, this);
        this.comments.add(newComment);
    }
}
