package com.patrol.domain.image.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patrol.api.ai.AiClient;
import com.patrol.domain.image.entity.Image;
import com.patrol.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final AiClient aiClient;
    private final ImageEventProducer imageEventProducer;

    public void sendImageEvent(Long imageId, String imagePath) {
        log.info("이미지 이벤트 전송: ID={}, Path={}", imageId, imagePath);
        imageEventProducer.sendImageEvent(imageId, imagePath);
    }

    @Transactional
    public Image saveImage(Image image) {
        Image savedImage = imageRepository.save(image);
        log.info("이미지 저장 완료: ID={}, Path={}", savedImage.getId(), savedImage.getPath());
        return savedImage;
    }

    @Transactional
    public void processExistingImagesWithTransaction() {
        try {
            processExistingImages();
        } catch (Exception e) {
            log.error("processExistingImages() 실행 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    @Transactional
    public void processExistingFoundImagesWithTransaction() {
        try {
            processExistingFoundImages();
        } catch (Exception e) {
            log.error("processExistingFoundImages() 실행 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    public void processExistingImages() throws IOException {
        List<Image> imagesWithoutEmbedding = imageRepository.findByEmbeddingIsNull();
        log.info("임베딩이 없는 이미지 수: {}", imagesWithoutEmbedding.size());

        for (Image image : imagesWithoutEmbedding) {
            try {
                String imageUrl = image.getPath();
                log.debug("이미지 URL에서 임베딩 추출 요청: {}", imageUrl);

                // 임베딩 & 피처 추출
                Map<String, String> embeddingData = aiClient.extractEmbeddingAndFeaturesFromUrl(imageUrl);
                String embedding = embeddingData.get("embedding");
                String features = embeddingData.get("features");

                log.info("추출된 임베딩: {}", embedding);
                log.info("추출된 피처: {}", features);

                if (embedding == null || embedding.isEmpty()) {
                    log.error("임베딩 추출 실패: 이미지 ID {}, URL {}", image.getId(), imageUrl);
                    continue;
                }

                // DB 저장
                image.setEmbedding(embedding);
                image.setFeatures(features);
                imageRepository.save(image);

                log.info("DB 저장 완료: 이미지 ID {}, 임베딩 {}, 피처 {}", image.getId(), image.getEmbedding(), image.getFeatures());
            } catch (Exception e) {
                log.error("이미지 ID {}의 임베딩 추출 실패: {}", image.getId(), e.getMessage());
            }
        }
    }

    public void processExistingFoundImages() {
        List<Image> foundImages = imageRepository.findByFoundIdIsNotNullAndEmbeddingIsNotNull();
        log.info("발견 이미지 수: {}", foundImages.size());

        for (Image foundImage : foundImages) {
            try {
                Long foundId = foundImage.getFoundId();
                log.debug("발견 ID {}의 이미지 처리 중", foundId);

                List<Image> animalImages = imageRepository.findAllByAnimalIdIsNotNullAndEmbeddingIsNotNull();
                log.info("등록된 동물 임베딩 개수: {}", animalImages.size());

                if (animalImages.isEmpty()) {
                    log.info("등록된 동물 이미지가 없어 배치 비교를 건너뜁니다.");
                    continue;
                }

                List<AiClient.AnimalSimilarity> similarities = aiClient.batchCompareUrl(
                        foundImage.getPath(), convertImagesToEmbeddings(animalImages));

                log.info("발견 ID {}의 비교 완료, 결과 개수 {}", foundId, similarities.size());
            } catch (Exception e) {
                log.error("발견 ID {}의 이미지 처리 실패: {}", foundImage.getFoundId(), e.getMessage());
            }
        }
    }

    private Map<String, List<Double>> convertImagesToEmbeddings(List<Image> images) throws IOException {
        Map<String, List<Double>> embeddings = new HashMap<>();
        for (Image image : images) {
            List<Double> embeddingList = new ObjectMapper().readValue(
                    image.getEmbedding(),
                    new TypeReference<List<Double>>() {});

            embeddings.put(image.getAnimalId().toString(), embeddingList);
        }
        return embeddings;
    }
}
