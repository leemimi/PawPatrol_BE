package com.patrol.domain.image.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patrol.api.ai.AiClient;
import com.patrol.domain.animal.entity.Animal;
import com.patrol.domain.animal.repository.AnimalRepository;
import com.patrol.domain.image.entity.Image;
import com.patrol.domain.image.repository.ImageRepository;
import com.patrol.domain.lostFoundPost.entity.PostStatus;
import com.patrol.domain.lostFoundPost.repository.LostFoundPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageEventConsumer {
    private final ImageRepository imageRepository;
    private final AiClient aiClient;
    private final ObjectMapper objectMapper;
    private final AnimalRepository animalRepository;
    private final ImageProcessingService imageProcessingService;

    @KafkaListener(topics = "image-events", groupId = "image-embedding-processor")
    public void processImageEvent(@Payload String message) throws IOException {
        try {
            Map<String, String> event = objectMapper.readValue(message, new TypeReference<>() {});
            Long imageId = Long.parseLong(event.get("imageId"));
            String imageUrl = event.get("imageUrl");

            log.info("🔵 Kafka 이벤트 수신: imageId={}, imageUrl={}", imageId, imageUrl);

            Image image = imageRepository.findById(imageId)
                    .filter(img -> img.getStatus() != null)
                    .orElse(null);

            if (image == null) {
                log.info("🚨 이미지 ID {}는 Kafka 처리에서 제외됨", imageId);
                return;
            }

            Animal animal = animalRepository.findById(image.getAnimalId()).orElse(null);
            if (animal == null || !animal.isLost()) {
                log.info("🚨 동물이 실종되지 않음 -> Kafka 처리 제외: imageId={}", imageId);
                return;
            }

            // ✅ 임베딩이 이미 저장된 경우, 중복 처리 방지
            if (image.getEmbedding() != null) {
                log.info("🚨 이미지 ID {}는 이미 임베딩이 존재하므로 Kafka 재처리 방지", imageId);
                return;
            }

            log.info("🔍 AI 서버에 이미지 분석 요청: imageId={}", imageId);
            Map<String, String> embeddingData = aiClient.extractEmbeddingAndFeaturesFromUrl(imageUrl);

            if (!embeddingData.containsKey("embedding")) {
                throw new RuntimeException("🚨 임베딩 추출 실패: imageId=" + imageId);
            }

            saveImageEmbedding(imageId, embeddingData);

            // ✅ 유사도 분석을 비동기로 실행
            if (image.getStatus() == PostStatus.FINDING) {
                imageProcessingService.asyncProcessImageFind(imageId);
            } else if (image.getStatus() == PostStatus.SIGHTED) {
                imageProcessingService.asyncProcessSightedImage(imageId);
            }

        } catch (Exception e) {
            log.error("🚨 Kafka 메시지 처리 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    @Transactional
    public void saveImageEmbedding(Long imageId, Map<String, String> embeddingData) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("이미지를 찾을 수 없음: imageId=" + imageId));

        image.setEmbedding(embeddingData.get("embedding"));
        image.setFeatures(embeddingData.get("features"));
        imageRepository.save(image);
        log.info("✅ 임베딩 저장 완료: imageId={}", imageId);
    }

}
