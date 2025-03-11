package com.patrol.global.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;


@Slf4j
@Component
public class FCMInitializer {

    @Value("${fcm.credentials}")
    private String firebaseCredentials;

    @PostConstruct
    public void initialize() throws IOException {
        try (InputStream is = new ByteArrayInputStream(firebaseCredentials.getBytes())) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(is))
                    .build();

                FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath);
                options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
            } else if (googleApplicationCredentials != null && !googleApplicationCredentials.isEmpty()) {
                ClassPathResource resource = new ClassPathResource(googleApplicationCredentials);
                try (InputStream is = resource.getInputStream()) {
                    options = FirebaseOptions.builder()
                            .setCredentials(GoogleCredentials.fromStream(is))
                            .build();
                }
            } else {
                throw new RuntimeException("❌ Firebase 설정을 찾을 수 없습니다. 환경 변수(FIREBASE_CONFIG_PATH) 또는 application.yml(fcm.certification) 중 하나를 설정하세요.");
            }

            // FirebaseApp 초기화
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("🔥 FirebaseApp이 정상적으로 초기화되었습니다.");
            }
      
        } catch (IOException e) {
            log.error("❌ Firebase 초기화 실패: " + e.getMessage(), e);
            throw new RuntimeException("Firebase 초기화 중 오류 발생!", e);
        }
    }
}
