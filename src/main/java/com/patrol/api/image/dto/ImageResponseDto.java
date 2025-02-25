package com.patrol.api.image.dto;

import com.patrol.domain.image.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponseDto {
    private Long id;
    private String path;

    public ImageResponseDto(Image image) {
        this.id = image.getId();
        this.path = image.getPath();
    }
}

