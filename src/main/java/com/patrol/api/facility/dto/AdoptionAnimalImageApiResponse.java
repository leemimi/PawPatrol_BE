package com.patrol.api.facility.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdoptionAnimalImageApiResponse {
  @JsonProperty("TbAdpWaitAnimalPhotoView")
  private TbAdpWaitAnimalPhotoView tbAdpWaitAnimalPhotoView;

  @Getter
  @Setter
  public static class TbAdpWaitAnimalPhotoView {
    private int list_total_count;
    @JsonProperty("RESULT")
    private Result result;
    private List<Row> row;
  }

  @Getter
  @Setter
  public static class Result {
    @JsonProperty("CODE")
    private String code;
    @JsonProperty("MESSAGE")
    private String message;
  }

  @Getter
  @Setter
  public static class Row {
    @JsonProperty("ANIMAL_NO")
    private String animalNo;      // 동물고유번호
    @JsonProperty("PHOTO_KND")
    private String photoKind;     // 사진 종류 (THUMB: 썸네일, IMG: 이미지)
    @JsonProperty("PHOTO_NO")
    private Double photoNo;       // 사진 번호
    @JsonProperty("PHOTO_URL")
    private String photoUrl;      // 사진 URL
  }
}
