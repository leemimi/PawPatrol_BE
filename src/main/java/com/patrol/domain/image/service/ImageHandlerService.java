package com.patrol.domain.image.service;

import com.patrol.domain.image.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageHandlerService {
    // 이미지 저장 (Kafka 이벤트 발행 포함)
    String createImageUrl(String folderPath, String fileName);

    // 이미지 등록 (범용적인 메소드)
    Image registerImage(String imageUrl, Long animalId, Long foundId);

    // 여러 이미지 업로드 및 등록
    List<Image> uploadAndRegisterImages(List<MultipartFile> files, String folderPath, Long animalId, Long foundId);

    // 이미지 삭제
    void deleteImage(Image image);

    // 경로로 이미지 삭제
    void deleteImageByPath(String path);
}
