package com.patrol.domain.image.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patrol.api.ai.AiClient;
import com.patrol.domain.image.entity.Image;
import com.patrol.domain.image.repository.ImageRepository;
import com.patrol.global.error.ErrorCode;
import com.patrol.global.exception.CustomException;
import com.patrol.global.storage.FileStorageHandler;
import com.patrol.global.storage.FileUploadRequest;
import com.patrol.global.storage.FileUploadResult;
import com.patrol.global.storage.NcpObjectStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    private final ImageRepository imageRepository;
    private final AiClient aiClient;
    private final ImageEventProducer imageEventProducer;
    private final FileStorageHandler fileStorageHandler;
    private final NcpObjectStorageService ncpObjectStorageService;
    private final ObjectMapper objectMapper;

    @Value("${ncp.storage.endpoint}")
    private String endPoint;

    @Value("${ncp.storage.bucketname}")
    private String bucketName;

    private static final String ANIMAL_FOLDER_PATH = "protection/";
    private static final String LOSTFOUND_FOLDER_PATH = "lostfoundpost/";

    /**
     * 이미지 이벤트 전송 메소드
     */
    public void sendImageEvent(Long imageId, String imagePath) {
        log.info("이미지 이벤트 전송: ID={}, Path={}", imageId, imagePath);
        imageEventProducer.sendImageEvent(imageId, imagePath);
    }

    /**
     * 동물 이미지 업로드
     */
    @Transactional
    public List<Image> uploadAnimalImages(List<MultipartFile> images, Long animalId) {
        log.info("동물 이미지 업로드: animalId={}, 이미지 개수={}", animalId, images.size());
        return uploadImages(images, ANIMAL_FOLDER_PATH, animalId, null);
    }

    /**
     * 분실/발견 이미지 업로드
     */
    @Transactional
    public List<Image> uploadLostFoundImages(List<MultipartFile> images, Long foundId) {
        log.info("분실/발견 이미지 업로드: foundId={}, 이미지 개수={}", foundId, images.size());
        return uploadImages(images, LOSTFOUND_FOLDER_PATH, null, foundId);
    }

    /**
     * 이미지 저장
     */
    @Transactional
    public Image saveImage(Image image) {
        log.info("이미지 저장: Path={}", image.getPath());
        Image savedImage = imageRepository.save(image);
        log.info("이미지 저장 완료: ID={}, Path={}", savedImage.getId(), savedImage.getPath());
        return savedImage;
    }

    /**
     * 단일 이미지 업로드 및 URL 반환
     */
    public String uploadImageAndGetUrl(MultipartFile image, String folderPath) {
        log.info("단일 이미지 업로드: folderPath={}", folderPath);
        try {
            FileUploadResult uploadResult = fileStorageHandler.handleFileUpload(
                    FileUploadRequest.builder()
                            .folderPath(folderPath)
                            .file(image)
                            .build()
            );

            if (uploadResult != null) {
                String imageUrl = endPoint + "/" + bucketName + "/" + folderPath + uploadResult.getFileName();
                log.info("이미지 업로드 완료: URL={}", imageUrl);
                return imageUrl;
            }
            return null;
        } catch (Exception e) {
            log.error("이미지 업로드 실패: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    /**
     * 여러 이미지 업로드 및 DB 저장
     */
    @Transactional
    public List<Image> uploadImages(List<MultipartFile> images, String folderPath, Long animalId, Long foundId) {
        log.info("이미지 다중 업로드: folderPath={}, animalId={}, foundId={}, 이미지 개수={}",
                folderPath, animalId, foundId, images.size());

        List<String> uploadedPaths = new ArrayList<>();
        List<Image> uploadedImages = new ArrayList<>();

        try {
            for (MultipartFile image : images) {
                FileUploadResult uploadResult = fileStorageHandler.handleFileUpload(
                        FileUploadRequest.builder()
                                .folderPath(folderPath)
                                .file(image)
                                .build()
                );

                if (uploadResult != null) {
                    String fileName = uploadResult.getFileName();
                    String fullPath = folderPath + fileName;
                    uploadedPaths.add(fullPath);

                    String imageUrl = endPoint + "/" + bucketName + "/" + fullPath;

                    Image imageEntity = Image.builder()
                            .path(imageUrl)
                            .animalId(animalId)
                            .foundId(foundId)
                            .build();

                    Image savedImage = imageRepository.save(imageEntity);
                    uploadedImages.add(savedImage);
                    log.info("이미지 업로드 및 저장: ID={}, URL={}", savedImage.getId(), imageUrl);
                }
            }
            return uploadedImages;
        } catch (Exception e) {
            log.error("이미지 업로드 실패, 롤백 중: {}", e.getMessage(), e);
            // 업로드된 파일 롤백
            for (String path : uploadedPaths) {
                try {
                    ncpObjectStorageService.delete(path);
                    log.info("업로드된 이미지 롤백 완료: Path={}", path);
                } catch (Exception ex) {
                    log.error("업로드된 이미지 롤백 실패: Path={}, 에러={}", path, ex.getMessage());
                }
            }
            throw new CustomException(ErrorCode.DATABASE_ERROR);
        }
    }

    /**
     * 이미지 다중 삭제
     */
    @Transactional
    public void deleteImages(List<Image> images) {
        log.info("이미지 다중 삭제: 이미지 개수={}", images.size());
        images.forEach(image -> {
            try {
                ncpObjectStorageService.delete(image.getPath());
                imageRepository.delete(image);
                log.info("이미지 삭제 완료: ID={}, Path={}", image.getId(), image.getPath());
            } catch (Exception e) {
                log.error("이미지 삭제 실패: ID={}, Path={}, 에러={}", image.getId(), image.getPath(), e.getMessage());
            }
        });
    }

    /**
     * 임베딩이 없는 이미지 처리
     */
    @Transactional
    public void processExistingImagesWithTransaction() {
        log.info("트랜잭션 내에서 임베딩 없는 이미지 처리 시작");
        try {
            processExistingImages();
            log.info("트랜잭션 내에서 임베딩 없는 이미지 처리 완료");
        } catch (Exception e) {
            log.error("임베딩 없는 이미지 처리 실패: {}", e.getMessage(), e);
        }
    }

    /**
     * 임베딩이 없는 이미지 처리 - AI 서비스에 요청하여 임베딩 생성
     */
    @Transactional
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

                if (embedding == null || embedding.isEmpty()) {
                    log.error("임베딩 추출 실패: 이미지 ID {}, URL {}", image.getId(), imageUrl);
                    continue;
                }

                // DB 저장
                image.setEmbedding(embedding);
                image.setFeatures(features);
                imageRepository.save(image);

                log.info("임베딩 저장 완료: 이미지 ID={}", image.getId());
                log.debug("임베딩 세부 정보: 임베딩={}, 피처={}", embedding, features);
            } catch (Exception e) {
                log.error("이미지 ID {}의 임베딩 추출 실패: {}", image.getId(), e.getMessage());
            }
        }
    }

    /**
     * 발견된 이미지 처리 - 등록된 동물 이미지와 비교
     */
    @Transactional
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

                Map<String, List<Double>> embeddings = convertImagesToEmbeddings(animalImages);
                List<AiClient.AnimalSimilarity> similarities = aiClient.batchCompareUrl(
                        foundImage.getPath(), embeddings);

                log.info("발견 ID {}의 비교 완료, 결과 개수 {}", foundId, similarities.size());
                // TODO: 유사도 결과 처리 로직 추가
            } catch (Exception e) {
                log.error("발견 ID {}의 이미지 처리 실패: {}", foundImage.getFoundId(), e.getMessage());
            }
        }
    }

    /**
     * 이미지에서 임베딩 추출
     */
    private Map<String, List<Double>> convertImagesToEmbeddings(List<Image> images) throws IOException {
        Map<String, List<Double>> embeddings = new HashMap<>();
        for (Image image : images) {
            if (image.getEmbedding() != null && !image.getEmbedding().isEmpty()) {
                List<Double> embeddingList = objectMapper.readValue(
                        image.getEmbedding(),
                        new TypeReference<List<Double>>() {});

                embeddings.put(image.getAnimalId().toString(), embeddingList);
            }
        }
        return embeddings;
    }

    public List<Image> findAllByAnimalId(Long animalId) {
      return imageRepository.findAllByAnimalId(animalId);
    }
}
