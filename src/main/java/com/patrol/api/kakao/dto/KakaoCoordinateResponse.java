package com.patrol.api.kakao.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * packageName    : com.patrol.api.kakao.dto
 * fileName       : KakaoCoordinateResponse
 * author         : sungjun
 * date           : 2025-03-14
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-14        kyd54       최초 생성
 */
@Getter
@Setter
public class KakaoCoordinateResponse {
    private List<Document> documents;

    @Getter
    @Setter
    public static class Document {
        private String address_name;
        private String x;  // 경도(longitude)
        private String y;  // 위도(latitude)
        private Address address;
        private RoadAddress road_address;
    }

    @Getter
    @Setter
    public static class Address {
        private String address_name;
        // 필요한 다른 필드들
    }

    @Getter
    @Setter
    public static class RoadAddress {
        private String address_name;
        // 필요한 다른 필드들
    }
}
