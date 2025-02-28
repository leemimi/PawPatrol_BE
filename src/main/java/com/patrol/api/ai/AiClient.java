package com.patrol.api.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class AiClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${ai.service.url}")
    private String aiServiceUrl;

    public Map<String, String> extractEmbeddingAndFeaturesFromUrl(String imageUrl) throws IOException {
        log.info("🔍 AI 서비스 임베딩 추출 시작: {}", imageUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("url", imageUrl);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        try {
            String endpoint = aiServiceUrl + "/extract-embedding-from-url";
            log.info("📡 AI 서비스 요청: POST {}", endpoint);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    endpoint,
                    requestEntity,
                    String.class
            );

            log.info("📄 AI 서비스 응답 상태: {}", response.getStatusCode());

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());

                if (jsonNode.has("success") && jsonNode.get("success").asBoolean()) {
                    Map<String, String> result = new HashMap<>();
                    String embedding = jsonNode.get("embedding").toString();
                    String features = jsonNode.get("features").toString();

                    // 임베딩 데이터 길이 로깅 (전체 내용은 너무 길어서 길이만 로깅)
                    log.info("✅ 임베딩 추출 성공: 길이={}", embedding.length());
                    log.info("✅ 피처 추출 성공: {}", features);

                    result.put("embedding", embedding);
                    result.put("features", features);
                    return result;
                } else {
                    String errorMessage = jsonNode.has("message") ?
                            jsonNode.get("message").asText() : "알 수 없는 오류";
                    log.error("❌ AI 서비스 처리 실패: {}", errorMessage);
                    throw new IOException("URL에서 임베딩 추출 실패: " + errorMessage);
                }
            } else {
                log.error("❌ AI 서비스 응답 오류: {}", response.getStatusCode());
                throw new IOException("URL 임베딩 추출 API 호출 실패: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            log.error("❌ AI 서비스 통신 오류: {}", e.getMessage(), e);
            throw new IOException("AI 서비스 연결 오류: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("❌ 예상치 못한 오류 발생: {}", e.getMessage(), e);
            throw new IOException("임베딩 추출 중 예외 발생: " + e.getMessage(), e);
        }
    }


    public List<AnimalSimilarity> batchCompareUrl(String path, Map<String, List<Double>> animalEmbeddings) throws IOException {
        log.info("🔍 이미지 유사도 배치 비교 시작: URL={}, 비교 대상 수={}", path, animalEmbeddings.size());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("url", path);
        String embeddingsJson = objectMapper.writeValueAsString(animalEmbeddings);
        body.add("embeddings_json", embeddingsJson);

        log.debug("📦 변환된 임베딩 JSON 길이: {}", embeddingsJson.length());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        try {
            String endpoint = aiServiceUrl + "/batch-compare-url";
            log.info("📡 AI 서비스 배치 비교 요청: POST {}", endpoint);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    endpoint,
                    requestEntity,
                    String.class);

            log.info("📄 AI 서비스 응답 상태: {}", response.getStatusCode());

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());

                if (!jsonNode.has("results")) {
                    log.error("❌ AI 서비스 응답에 results 필드가 없습니다");
                    throw new IOException("배치 비교 결과가 올바른 형식이 아닙니다");
                }

                ArrayNode resultsNode = (ArrayNode) jsonNode.get("results");
                log.info("✅ 배치 비교 결과 수신: {} 개의 결과", resultsNode.size());

                List<AnimalSimilarity> results = new ArrayList<>();
                for (JsonNode resultNode : resultsNode) {
                    AnimalSimilarity similarity = new AnimalSimilarity(
                            resultNode.get("animal_id").asText(),
                            resultNode.get("similarity").asDouble(),
                            resultNode.get("is_match").asBoolean()
                    );
                    results.add(similarity);

                    if (similarity.isMatch()) {
                        log.info("🔍 일치 발견: animalId={}, 유사도={}",
                                similarity.getAnimalId(), similarity.getSimilarity());
                    }
                }

                return results;
            } else {
                log.error("❌ AI 서비스 응답 오류: {}", response.getStatusCode());
                throw new IOException("URL 배치 비교 API 호출 실패: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            log.error("❌ AI 서비스 통신 오류: {}", e.getMessage(), e);
            throw new IOException("AI 서비스 연결 오류: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("❌ 예상치 못한 오류 발생: {}", e.getMessage(), e);
            throw new IOException("배치 비교 중 예외 발생: " + e.getMessage(), e);
        }
    }

    @Data
    @AllArgsConstructor
    public static class AnimalSimilarity {
        private String animalId;
        private double similarity;
        private boolean isMatch;
    }
}
