package com.patrol.domain.ai.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patrol.domain.ai.entity.AiImage;
import com.patrol.domain.ai.repository.AiImageRepository;
import com.patrol.domain.lostFoundPost.entity.PostStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageProcessingService {
    private final AiImageRepository aiImageRepository;
    private final PythonMLService pythonMLService;
    private final ObjectMapper objectMapper;
    private final AiImageService aiImageService;
    private final AiClient aiClient;

    @Async
    public void asyncProcessImageFind(Long imageId) {
        processImageSimilarity(imageId, PostStatus.FINDING);
    }

    @Async
    public void asyncProcessSightedImage(Long imageId) {
        processImageSimilarity(imageId, PostStatus.SIGHTED);
    }

    @Transactional
    public void processImageSimilarity(Long imageId, PostStatus targetStatus) {
        try {
            log.info("📩 Kafka 메시지 수신 (유사도 분석 - {} 기준): imageId={}", targetStatus, imageId);

            AiImage newImage = aiImageRepository.findById(imageId)
                    .orElseThrow(() -> new RuntimeException("🚨 이미지 ID " + imageId + "를 찾을 수 없음"));

            if (newImage.getEmbedding() == null) {
                log.info("🚀 AI 분석 요청 (비동기) 시작: imageId={}", imageId);

                CompletableFuture<Map<String, String>> embeddingFuture = aiClient.extractEmbeddingAsync(newImage.getPath());

                embeddingFuture.thenAccept(embeddingData -> {
                    if (embeddingData.containsKey("embedding")) {
                        newImage.setEmbedding(embeddingData.get("embedding"));
                        newImage.setFeatures(embeddingData.get("features"));
                        aiImageRepository.save(newImage);
                        log.info("✅ 임베딩 데이터 저장 완료 (비동기): imageId={}", imageId);
                    } else {
                        log.error("🚨 임베딩 추출 실패: imageId={}", imageId);
                    }
                });
                return;
            }

            PostStatus oppositeStatus = (targetStatus == PostStatus.FINDING) ? PostStatus.SIGHTED : PostStatus.FINDING;

            List<AiImage> nearbyTargetImages = aiImageRepository.findNearbyAiImages(
                            newImage.getLostFoundPost().getLatitude(),
                            newImage.getLostFoundPost().getLongitude(),
                            10.0
                    ).stream()
                    .filter(img -> img.getStatus() == oppositeStatus)
                    .toList();

            nearbyTargetImages = nearbyTargetImages.stream()
                    .filter(img -> img.getEmbedding() != null)
                    .toList();

            log.info("🔍 유사도 분석 대상: {}개", nearbyTargetImages.size());

            for (AiImage targetImage : nearbyTargetImages) {
                double similarity = pythonMLService.compareEmbeddingsAndFeatures(
                        extractEmbeddingAsList(newImage.getEmbedding()),
                        extractEmbeddingAsList(newImage.getFeatures()),
                        extractEmbeddingAsList(targetImage.getEmbedding()),
                        extractEmbeddingAsList(targetImage.getFeatures())
                );

                if (similarity >= 0.85) {
                    log.info("🔍 유사한 게시글 발견! targetId={}, 유사도={}", targetImage.getId(), similarity);
                    aiImageService.linkSightedToFindingPost(newImage, targetImage, similarity);
                }
            }
        } catch (Exception e) {
            log.error("🚨 Kafka 메시지 처리 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    private List<Double> extractEmbeddingAsList(String jsonEmbedding) {
        try {
            return jsonEmbedding != null ? objectMapper.readValue(jsonEmbedding, new TypeReference<>() {}) : Collections.emptyList();
        } catch (Exception e) {
            log.error("🚨 임베딩 변환 실패: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
