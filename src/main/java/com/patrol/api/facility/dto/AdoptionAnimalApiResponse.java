package com.patrol.api.facility.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdoptionAnimalApiResponse {
  @JsonProperty("TbAdpWaitAnimalView")
  private TbAdpWaitAnimalView tbAdpWaitAnimalView;

  @Getter
  @Setter
  public static class TbAdpWaitAnimalView {
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
    private String animalNo;       // 동물고유번호
    @JsonProperty("NM")
    private String name;           // 이름
    @JsonProperty("ENTRNC_DATE")
    private String entranceDate;   // 입소날짜
    @JsonProperty("SPCS")
    private String species;        // 종
    @JsonProperty("BREEDS")
    private String breed;          // 품종
    @JsonProperty("SEXDSTN")
    private String gender;         // 성별
    @JsonProperty("AGE")
    private String age;            // 나이
    @JsonProperty("BDWGH")
    private String weight;         // 체중
    @JsonProperty("ADP_STTUS")
    private String adoptionStatus; // 입양상태 (N: 입양대기, P: 입양진행중, C: 입양완료)
    @JsonProperty("TMPR_PRTC_STTUS")
    private String tempProtectStatus; // 임시보호상태 (N: 센터보호중, C: 임시보호중)
    @JsonProperty("INTRCN_MVP_URL")
    private String videoUrl;       // 소개동영상URL
    @JsonProperty("INTRCN_CN")
    private String description;    // 소개내용
    @JsonProperty("TMPR_PRTC_CN")
    private String tempProtectInfo; // 임시보호내용
  }
}

