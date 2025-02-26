package com.patrol.domain.image.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "found_id", nullable = true)
    private Long foundId;

    @Column(name= "animal_id", nullable= true)
    private Long animalId;

    @Builder
    public Image(String path, Long animalId, Long foundId) {
        this.path = path;
        this.animalId=animalId;
        this.foundId = foundId;
        this.createdAt = LocalDateTime.now();
    }
}