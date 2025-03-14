package com.patrol.domain.kakao.service;

import com.patrol.api.kakao.dto.KakaoAddressResponse;
import com.patrol.api.kakao.dto.KakaoCoordinateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class KakaoApiService {

  private final RestTemplate restTemplate;

  @Value("${kakao.rest-api-key}")
  private String kakaoRestApiKey;

  private static final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/geo/coord2address.json";
  private static final String  KAKAO_ADDRESS_SEARCH_URL = "https://dapi.kakao.com/v2/local/search/address.json";

  public KakaoAddressResponse getAddressFromCoords(double longitude, double latitude) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);

    URI uri = UriComponentsBuilder.fromHttpUrl(KAKAO_API_URL)
        .queryParam("x", longitude)
        .queryParam("y", latitude)
        .build()
        .toUri();

    HttpEntity<?> entity = new HttpEntity<>(headers);

    ResponseEntity<KakaoAddressResponse> response = restTemplate.exchange(
        uri,
        HttpMethod.GET,
        entity,
        KakaoAddressResponse.class
    );

    return response.getBody();
  }

  // 도로명 주소로 좌표 검색
  public KakaoCoordinateResponse getCoordsFromAddress(String address) {
    if (address == null || address.trim().isEmpty()) {
      System.out.println("주소가 비어있습니다.");
      return null;
    }

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);
    headers.set("Content-Type", "application/json;charset=UTF-8"); // UTF-8 명시적 지정

    URI uri = UriComponentsBuilder.fromHttpUrl(KAKAO_ADDRESS_SEARCH_URL)
            .queryParam("query", address)
            .build()
            .encode(StandardCharsets.UTF_8) // 명시적 인코딩 추가
            .toUri();

    System.out.println("Kakao API 요청 URL: " + uri);

    HttpEntity<?> entity = new HttpEntity<>(headers);

    try {
      ResponseEntity<KakaoCoordinateResponse> response = restTemplate.exchange(
              uri,
              HttpMethod.GET,
              entity,
              KakaoCoordinateResponse.class
      );
      return response.getBody();
    } catch (HttpClientErrorException e) {
      System.out.println("Kakao API 호출 오류: " + e.getStatusCode() + " " + e.getResponseBodyAsString()); // ✅ 에러 본문 출력
      return null;
    }
  }
}
