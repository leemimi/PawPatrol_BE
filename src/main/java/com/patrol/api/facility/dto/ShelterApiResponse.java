package com.patrol.api.facility.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShelterApiResponse {

  private Response response;

  @Getter @Setter
  public static class Response {
    private Header header;
    private Body body;
  }

  private Header header;
  private Body body;

  @Getter
  @Setter
  public static class Header {
    private String resultCode;
    private String resultMsg;
  }

  @Getter
  @Setter
  public static class Body {
    private Items items;
    private int numOfRows;
    private int pageNo;
    private int totalCount;
  }

  @Getter
  @Setter
  public static class Items {
    private List<Item> item;
  }

  @Getter
  @Setter
  public static class Item {
    private String careNm;
    private String orgNm;
    private String divisionNm;
    private String careAddr;
    private String careTel;
    private String saveTrgtAnimal;
    private String jibunAddr;
    private Double lat;
    private Double lng;
    private String dsignationDate;
    private String weekOprStime;
    private String weekOprEtime;
    private String weekendOprStime;
    private String weekendOprEtime;
    private String closeDay;
    private Integer vetPersonCnt;
    private Integer specsPersonCnt;
    private String dataStdDt;
  }
}
