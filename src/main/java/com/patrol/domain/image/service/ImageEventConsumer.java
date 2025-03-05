package com.patrol.domain.image.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patrol.api.ai.AiClient;
import com.patrol.domain.image.entity.Image;
import com.patrol.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageEventConsumer {
    private final ImageRepository imageRepository;
    private final AiClient aiClient;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "image-events", groupId = "image-processor-group")
    @Transactional
    public void processImageEvent(@Payload String message) {
        log.info("📩 Kafka 메시지 수신: {}", message);

        try {
            // Kafka 메시지 파싱
            Map<String, String> event = objectMapper.readValue(message, new TypeReference<>() {});
            Long imageId = Long.parseLong(event.get("imageId"));
            String imageUrl = event.get("imageUrl");

            log.info("🔵 Kafka 이벤트 처리 시작: imageId={}, imageUrl={}", imageId, imageUrl);

            // DB에서 이미지 조회
            Image image = imageRepository.findById(imageId)
                    .orElseThrow(() -> new RuntimeException("이미지 ID " + imageId + "를 찾을 수 없음"));

            // 임베딩 및 피처 추출
            log.info("🔍 AI 서버에 이미지 분석 요청: imageId={}", imageId);
            Map<String, String> embeddingData = aiClient.extractEmbeddingAndFeaturesFromUrl(imageUrl);
            String embedding = embeddingData.get("embedding");
            String features = embeddingData.get("features");

            if (embedding == null || embedding.isEmpty()) {
                log.error("🚨 임베딩 추출 실패: 이미지 ID {}, URL {}", imageId, imageUrl);
                return;
            }

            log.info("💾 임베딩 데이터 저장 시작: imageId={}", imageId);
            // DB에 저장
            image.setEmbedding(embedding);
            image.setFeatures(features);
            imageRepository.save(image);

            log.info("🟢 Kafka 처리 완료: 이미지 ID {}, 임베딩 저장됨", imageId);
        } catch (Exception e) {
            log.error("🚨 Kafka 메시지 처리 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}
