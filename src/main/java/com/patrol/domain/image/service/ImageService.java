package com.patrol.domain.image.service;

import com.patrol.domain.image.entity.Image;
import com.patrol.domain.image.repository.ImageRepository;
import com.patrol.global.error.ErrorCode;
import com.patrol.global.exception.CustomException;
import com.patrol.global.storage.FileStorageHandler;
import com.patrol.global.storage.FileUploadRequest;
import com.patrol.global.storage.FileUploadResult;
import com.patrol.global.storage.NcpObjectStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

  private final FileStorageHandler fileStorageHandler;
  private final ImageRepository imageRepository;
  private final NcpObjectStorageService ncpObjectStorageService;

  @Value("${ncp.storage.endpoint}")
  private String endPoint;

  public List<Image> uploadAnimalImages(List<MultipartFile> images, Long animalId) {
    return uploadImages(images, "protection/", animalId, null);
  }

  public List<Image> uploadLostFoundImages(List<MultipartFile> images, Long foundId) {
    return uploadImages(images, "lostfoundpost/", null, foundId);
  }

  private List<Image> uploadImages(List<MultipartFile> images, String folderPath, Long animalId, Long foundId) {
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
          uploadedPaths.add(fileName);

          Image imageEntity = Image.builder()
              .path(endPoint + "/paw-patrol/" + folderPath + fileName)
              .animalId(animalId)
              .foundId(foundId)
              .build();

          imageRepository.save(imageEntity);
          uploadedImages.add(imageEntity);
        }
      }
      return uploadedImages;
    } catch (Exception e) {
      for (String path : uploadedPaths) {
        ncpObjectStorageService.delete(path);
      }
      throw new CustomException(ErrorCode.DATABASE_ERROR);
    }
  }


  @Transactional
  public void deleteImages(List<Image> images) {
    images.forEach(image -> {
      ncpObjectStorageService.delete(image.getPath());
      imageRepository.delete(image);
    });
  }

  public String uploadImageAndGetUrl(MultipartFile image, String folderPath) {
    try {
      FileUploadResult uploadResult = fileStorageHandler.handleFileUpload(
          FileUploadRequest.builder()
              .folderPath(folderPath)
              .file(image)
              .build()
      );

      if (uploadResult != null) {
        return endPoint + "/paw-patrol/" + folderPath + uploadResult.getFileName();
      }
      return null;
    } catch (Exception e) {
      throw new CustomException(ErrorCode.DATABASE_ERROR);
    }
  }
}
