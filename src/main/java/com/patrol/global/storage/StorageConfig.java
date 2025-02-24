package com.patrol.global.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * packageName    : com.patrol.global.storage
 * fileName       : StorageConfig
 * author         : sungjun
 * date           : 2025-02-24
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-24        kyd54       최초 생성
 */
@Configuration
@ConfigurationProperties(prefix = "ncp.storage")
@Getter
@Setter
public class StorageConfig {
    private String endpoint;
    private String bucketname;
}
