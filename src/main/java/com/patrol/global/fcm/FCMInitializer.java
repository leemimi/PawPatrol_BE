package com.patrol.global.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("FirebaseApp initialization complete");
            }
        } catch (Exception e) {
            log.error("Error initializing Firebase: ", e);
            throw e;
        }
    }
}
