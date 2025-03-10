package com.patrol.domain.image.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patrol.domain.image.entity.Image;
import com.patrol.domain.image.repository.ImageRepository;
import com.patrol.domain.lostFoundPost.entity.LostFoundPost;
import com.patrol.domain.lostFoundPost.entity.PostStatus;
import com.patrol.domain.lostFoundPost.repository.LostFoundPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageProcessingService {
    private final ImageRepository imageRepository;
    private final LostFoundPostRepository lostFoundPostRepository;
    private final PythonMLService pythonMLService;
    private final ImageService imageService;
    private final ObjectMapper objectMapper;

    @Async
    public void asyncProcessImageFind(Long imageId) {
        processImageFind(imageId);
    }

    @Async
    public void asyncProcessSightedImage(Long imageId) {
        processSightedImage(imageId);
    }

    @Transactional
    public void processImageFind(Long imageId) {
        try {
            log.info("📩 Kafka 메시지 수신 (유사도 분석 - FINDING 기준): imageId={}", imageId);

            Image findingImage = imageRepository.findById(imageId)
                    .filter(img -> img.getStatus() == PostStatus.FINDING)
                    .orElseThrow(() -> new RuntimeException("이미지 ID 실종글 " + imageId + "를 찾을 수 없음"));

            LostFoundPost findingPost = lostFoundPostRepository.findById(findingImage.getFoundId())
                    .orElseThrow(() -> new RuntimeException("FINDING 게시글을 찾을 수 없음"));

            List<LostFoundPost> sightedPosts = lostFoundPostRepository.findSightedPostsWithinRadius(
                    findingPost.getLatitude(), findingPost.getLongitude(), 5.0, findingPost.getAnimalType().name()
            );

            for (LostFoundPost sightedPost : sightedPosts) {
                Image sightedImage = imageRepository.findByFoundId(sightedPost.getId());
                if (sightedImage == null || sightedImage.getStatus() == null) continue;

                double similarity = pythonMLService.compareEmbeddingsAndFeatures(
                        extractEmbeddingAsList(findingImage.getEmbedding()),
                        extractEmbeddingAsList(findingImage.getFeatures()),
                        extractEmbeddingAsList(sightedImage.getEmbedding()),
                        extractEmbeddingAsList(sightedImage.getFeatures())
                );

                if (similarity >= 0.85) {
                    log.info("🔍 유사한 게시글 발견! sightedId={}, 유사도={}", sightedPost.getId(), similarity);
                    imageService.linkSightedToFindingPost(findingImage, sightedImage, similarity);
                }
            }

        } catch (Exception e) {
            log.error("🚨 Kafka 메시지 처리 중 오류 발생: {}", e.getMessage(), e);
        }
    }
    @Transactional
    public void processSightedImage(Long imageId) {
        try {
            log.info("📩 Kafka 메시지 수신 (유사도 분석 - SIGHTED 기준): imageId={}", imageId);

            Image sightedImage = imageRepository.findById(imageId)
                    .filter(img -> img.getStatus() == PostStatus.SIGHTED)
                    .orElseThrow(() -> new RuntimeException("🚨 이미지 ID 제보글 " + imageId + "를 찾을 수 없음"));

            LostFoundPost sightedPost = lostFoundPostRepository.findById(sightedImage.getFoundId())
                    .orElseThrow(() -> new RuntimeException("🚨 SIGHTED 게시글을 찾을 수 없음"));

            log.info("🔵 Kafka 이벤트 처리 시작 (유사도 분석 - SIGHTED 기준): imageId={}, postId={}", imageId, sightedPost.getId());

            List<LostFoundPost> findingPosts = lostFoundPostRepository.findFindingPostsWithinRadius(
                    sightedPost.getLatitude(), sightedPost.getLongitude(), 5.0, sightedPost.getAnimalType().name()
            );

            for (LostFoundPost findingPost : findingPosts) {
                Image findingImage = imageRepository.findByFoundId(findingPost.getId());
                if (findingImage == null || findingImage.getStatus() == null) continue;

                double similarity = pythonMLService.compareEmbeddingsAndFeatures(
                        extractEmbeddingAsList(sightedImage.getEmbedding()),
                        extractEmbeddingAsList(sightedImage.getFeatures()),
                        extractEmbeddingAsList(findingImage.getEmbedding()),
                        extractEmbeddingAsList(findingImage.getFeatures())
                );

                if (similarity >= 0.85) {
                    log.info("🔍 유사한 실종글 발견! findingId={}, sightedId={}, 유사도={}", findingPost.getId(), sightedPost.getId(), similarity);
                    imageService.linkSightedToFindingPost(findingImage, sightedImage, similarity);
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
