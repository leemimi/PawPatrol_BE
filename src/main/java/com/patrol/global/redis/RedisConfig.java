package com.patrol.global.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

// Redis 설정을 위한 Config, Redis 연결 및 템플릿 설정을 담당
@Configuration
public class RedisConfig {
  // Redis 호스트 주소
  @Value("${spring.data.redis.host}")
  private String host;

  // Redis 포트 번호
  @Value("${spring.data.redis.port}")
  private int port;

  // Redis 비밀번호 (설정되지 않았다면 빈 문자열)
  @Value("${spring.data.redis.password:}")
  private String password;

  // Redis 연결을 위한 ConnectionFactory 빈 설정
  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);

    if (!password.isEmpty()) {  // 비밀번호가 설정되어 있을 경우만 적용
      config.setPassword(password);
    }
    return new LettuceConnectionFactory(config);
  }

  // Redis 데이터 접근을 위한 템플릿 클래스
  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory());

    // 직렬화 설정
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new StringRedisSerializer());
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(new StringRedisSerializer());

    return redisTemplate;
  }

  @Bean
  public StringRedisTemplate stringRedisTemplate() {
    return new StringRedisTemplate(redisConnectionFactory());
  }
}
