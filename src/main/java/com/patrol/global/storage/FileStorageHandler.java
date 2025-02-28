package com.patrol.global.storage;

import com.patrol.global.error.ErrorCode;
import com.patrol.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class FileStorageHandler {

    @Value("${ncp.storage.bucketname}")
    private String bucketName;
    private final StorageService storageService;

    // Update your FileStorageHandler.handleFileUpload method
    public FileUploadResult handleFileUpload(FileUploadRequest request) {
        try {
            // 파일 확장자 검증
            String contentType = request.getFile().getContentType();
            List<String> allowedTypes = Arrays.asList("image/jpeg", "image/png", "image/gif", "image/jpg");
            if (!allowedTypes.contains(contentType)) {
                throw new CustomException(ErrorCode.INVALID_FILE_TYPE);
            }

            // 파일 크기 검증 (5MB 제한)
            long maxSize = 5 * 1024 * 1024; // 5MB
            if (request.getFile().getSize() > maxSize) {
                throw new CustomException(ErrorCode.FILE_SIZE_EXCEEDED);
            }

            // 새 파일 업로드
            String filename = UUID.randomUUID().toString();
            HashMap<String, Object> options = new HashMap<>();
            options.put(StorageService.CONTENT_TYPE, request.getFile().getContentType());

            // Add content length to options - THIS IS THE KEY CHANGE
            options.put("contentLength", request.getFile().getSize());

            storageService.upload(
                    request.getFolderPath() + filename,
                    request.getFile().getInputStream(),
                    options
            );

            return FileUploadResult.builder()
                    .fileName(filename)
                    .fullPath(request.getFolderPath() + filename)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        }
    }

    // 파일 삭제
    public void handleFileDelete(String url) {
        try {
            // URL이 null이거나 비어있는 경우 처리
            if (url == null || url.isEmpty()) {
                throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
            }

            // 버킷 이름은 @Value로 주입받은 값 사용
            // 버킷 이름 이후의 경로를 fileName으로 사용
            String fileName = url.substring(url.indexOf(bucketName) + bucketName.length() + 1);

            // StorageService를 통해 파일 삭제
            storageService.delete(fileName);
        } catch (Exception e) {
            throw new RuntimeException("파일 삭제에 실패했습니다.", e);
        }
    }
}