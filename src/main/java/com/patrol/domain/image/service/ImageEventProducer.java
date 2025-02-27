package com.patrol.domain.image.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "image-events"; // Kafka 토픽 이름

    public void sendImageEvent(Long imageId, String imageUrl) {
        try {
            Map<String, String> event = new HashMap<>();
            event.put("imageId", imageId.toString());
            event.put("imageUrl", imageUrl);

            String eventJson = objectMapper.writeValueAsString(event);

            // Kafka 메시지 전송 (CompletableFuture 사용)
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC, eventJson);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("🚨 Kafka 이벤트 전송 실패: {}, 오류: {}", eventJson, ex.getMessage(), ex);
                } else {
                    RecordMetadata metadata = result.getRecordMetadata();
                    log.info("✅ Kafka 이벤트 전송 성공: {}, Partition: {}, Offset: {}", eventJson, metadata.partition(), metadata.offset());
                }
            });

        } catch (Exception e) {
            log.error("🚨 Kafka 메시지 변환 실패: {}", e.getMessage(), e);
        }
    }
}
