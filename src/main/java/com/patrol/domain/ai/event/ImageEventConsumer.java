package com.patrol.domain.ai.event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patrol.domain.ai.entity.AiImage;
import com.patrol.domain.ai.repository.AiImageRepository;
import com.patrol.domain.ai.service.AiClient;
import com.patrol.domain.ai.service.ImageProcessingService;
import com.patrol.domain.lostFoundPost.entity.PostStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageEventConsumer {
    private final AiImageRepository aiImageRepository;
    private final AiClient aiClient;
    private final ObjectMapper objectMapper;
    private final ImageProcessingService imageProcessingService;
    private static long totalMessageSize = 0;
    private static final AtomicLong totalProcessingTime = new AtomicLong(0);
    private static final AtomicLong messageCount = new AtomicLong(0);

    @KafkaListener(
            topics = "image-events",
            groupId = "${spring.kafka.groups.ai-group-id}"
    )
    public void processImageEvent(@Payload String message) throws IOException {
        totalMessageSize += message.getBytes().length;
        long startTime = System.currentTimeMillis();
        try {
            log.info("🔍 Consumer received message: {}", message);
            log.info("🔍🔍🔍🔍🔍 Counsumer에 도착!!!!!!!!!!!!!!!!!!!!!!!!!");
            log.error("🚨🚨🚨 KAFKA LISTENER ACTIVATED 🚨🚨🚨");
            log.error("🚨 Received message: {}", message);
            Map<String, String> event = objectMapper.readValue(message, new TypeReference<>() {});
            Long imageId = Long.parseLong(event.get("imageId"));
            String imageUrl = event.get("imageUrl");

            log.info("🔍 AI 서버에 이미지 분석 요청: imageId={}", imageId);
            Map<String, String> embeddingData = aiClient.extractEmbeddingAndFeaturesFromUrl(imageUrl);

            if (!embeddingData.containsKey("embedding")) {
                throw new RuntimeException("🚨 임베딩 추출 실패: imageId=" + imageId);
            }

            PostStatus postStatus = saveEmbeddingData(embeddingData, imageId);

            if (postStatus == PostStatus.FINDING) {
                log.info("📩 FINDING 이미지 유사도 분석 요청: imageId={}", imageId);
                imageProcessingService.asyncProcessImageFind(imageId);
            } else if (postStatus == PostStatus.SIGHTED) {
                log.info("📩 SIGHTED 이미지 유사도 분석 요청: imageId={}", imageId);
                imageProcessingService.asyncProcessSightedImage(imageId);
            }


        } catch (Exception e) {
            log.error("🚨 Kafka 메시지 처리 중 오류 발생: {}", e.getMessage(), e);
        }finally {
            long endTime = System.currentTimeMillis();  // 종료 시간 측정
            long processingTime = endTime - startTime; // 메시지 처리 시간 계산

            totalProcessingTime.addAndGet(processingTime);
            messageCount.incrementAndGet();

            log.info("⏱️ Kafka 메시지 처리 완료 (총 소요 시간): {}ms", processingTime);
        }
    }

    @Transactional
    protected PostStatus saveEmbeddingData (Map<String, String> embeddingData, Long imageId) {
        AiImage aiImage = aiImageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 이미지가 존재하지 않음: " + imageId));

        aiImage.setEmbedding(embeddingData.get("embedding"));
        aiImage.setFeatures(embeddingData.get("features"));
        aiImageRepository.save(aiImage);
        log.info("✅ 임베딩 데이터 저장 완료: imageId={}", imageId);
        return aiImage.getStatus();
    }
    @Scheduled(fixedRate = 60000) // 1분(60초)마다 실행
    public void logAverageProcessingTime() {
        long processedMessages = messageCount.get();
        if (processedMessages > 0) {
            long avgProcessingTime = totalProcessingTime.get() / processedMessages;
            log.info("📊 평균 Kafka 메시지 처리 속도: {}ms", avgProcessingTime);

            // 값 초기화
            totalProcessingTime.set(0);
            messageCount.set(0);
        } else {
            log.info("📊 현재 Kafka 메시지 처리 없음.");
        }
    }

}
