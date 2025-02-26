package com.patrol.api.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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

    public String extractEmbeddingFromUrl(String imageUrl) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("url", imageUrl);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                aiServiceUrl + "/extract-embedding-from-url",
                requestEntity,
                String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            if (jsonNode.has("success") && jsonNode.get("success").asBoolean()) {
                return jsonNode.get("embedding").toString();
            } else {
                throw new IOException("URL에서 임베딩 추출 실패");
            }
        } else {
            throw new IOException("URL 임베딩 추출 API 호출 실패: " + response.getStatusCode());
        }
    }

    public List<AnimalSimilarity> batchCompareUrl(String path, Map<String, List<Double>> animalEmbeddings) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("url", path);
        body.add("embeddings_json", objectMapper.writeValueAsString(animalEmbeddings));

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                aiServiceUrl + "/batch-compare-url",
                requestEntity,
                String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            ArrayNode resultsNode = (ArrayNode) jsonNode.get("results");

            List<AnimalSimilarity> results = new ArrayList<>();
            for (JsonNode resultNode : resultsNode) {
                results.add(new AnimalSimilarity(
                        resultNode.get("animal_id").asText(),
                        resultNode.get("similarity").asDouble(),
                        resultNode.get("is_match").asBoolean()
                ));
            }

            return results;
        } else {
            throw new IOException("URL 배치 비교 API 호출 실패: " + response.getStatusCode());
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
