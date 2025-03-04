package com.patrol.api.image.dto;

import com.patrol.domain.image.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponseDto {
    private String path;

    public ImageResponseDto(Image image) {
        this.path = image.getPath();
    }
}

