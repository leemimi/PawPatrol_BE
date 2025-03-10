package com.patrol.api.animalCase.dto;

import com.patrol.api.PostResponseDto.PostResponseDto;
import com.patrol.domain.animalCase.entity.AnimalCase;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnimalCaseResponseDto implements PostResponseDto {
    private Long id;
    private String title;
    private String description;

    public AnimalCaseResponseDto(AnimalCase post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.description = post.getDescription();
    }

    @Override
    public String getPostType() {
        return "ANIMALCASE";
    }
}
