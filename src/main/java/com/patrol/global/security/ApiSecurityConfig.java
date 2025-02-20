package com.patrol.global.security;


import com.patrol.global.app.AppConfig;
import com.patrol.global.oauth2.CustomAuthorizationRequestResolver;
import com.patrol.global.oauth2.CustomOAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class ApiSecurityConfig {

  private final CustomAuthenticationFilter customAuthenticationFilter;
  private final CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler;
  private final CustomAuthorizationRequestResolver customAuthorizationRequestResolver;

  @Bean
  public UrlBasedCorsConfigurationSource corsConfigurationSource() {
    // 위에서 설정한 CORS 설정 코드와 동일
    CorsConfiguration corsConfig = new CorsConfiguration();
    corsConfig.addAllowedOrigin(AppConfig.getSiteFrontUrl());
    corsConfig.addAllowedOrigin(AppConfig.getDevFrontUrl());
    corsConfig.addAllowedMethod("*");
    corsConfig.addAllowedHeader("*");

    corsConfig.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", corsConfig);
    source.registerCorsConfiguration("/oauth2/**", corsConfig);
    source.registerCorsConfiguration("/login/oauth2/**", corsConfig);
    source.registerCorsConfiguration("/login/**", corsConfig);

    return source;
  }

  // "/api/v1/auth/verification/email/send", "/api/v1/auth/verification/email/verify"
  @Bean
  SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/api/**", "/oauth2/**", "/login/oauth2/**", "/login/**")
        .authorizeHttpRequests(
            authorizeRequests -> authorizeRequests
                    .requestMatchers(HttpMethod.POST, "/api/lost-found/lost").permitAll()  // 실종 신고 게시글 등록
                    .requestMatchers(HttpMethod.PUT, "/api/lost-found/lost/{postId}").permitAll()  // 실종 신고 게시글 수정
                    .requestMatchers(HttpMethod.DELETE, "/api/lost-found/lost/{postId}").permitAll()  // 실종 신고 게시글 삭제
                    .requestMatchers(HttpMethod.GET, "/api/lost-found/lost").permitAll()  // 모든 실종 신고 게시글 조회
                    .requestMatchers(HttpMethod.GET, "/api/lost-found/lost/{postId}").permitAll()  // 실종 신고 게시글 상세 조회
                    .requestMatchers(HttpMethod.POST, "/api/lost-found").permitAll()  // 제보 게시글 등록
                    .requestMatchers(HttpMethod.PUT, "/api/lost-found/{postId}").permitAll()  // 신고글 연계 제보 게시글 수정
                    .requestMatchers(HttpMethod.DELETE, "/api/lost-found/{postId}").permitAll()  // 신고글 연계 제보 게시글 삭제
                    .requestMatchers(HttpMethod.GET, "/api/lost-found/find").permitAll()  // 모든 신고글 연계 제보 게시글 조회
                    .requestMatchers(HttpMethod.GET, "/api/lost-found/find-standalone").permitAll()  // 모든 독립적인 제보 게시글 목록 조회
                    .requestMatchers(HttpMethod.GET, "/api/lost-found/find-standalone/{postId}").permitAll()  // 독립적인 제보 게시글 상세 조회
                    .requestMatchers(HttpMethod.POST, "/api/lost-found/find-standalone").permitAll()  // 독립적인 제보 게시글 등록
                    .requestMatchers(HttpMethod.PUT, "/api/lost-found/find-standalone/{postId}").permitAll()  // 독립적인 제보 게시글 수정.requestMatchers(HttpMethod.DELETE, "/api/lost-found/find-standalone/{postId}").permitAll()  // 독립적인 제보 게시글 삭제
                .requestMatchers(HttpMethod.POST, "/api/*/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/*/auth/logout").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/*/auth/signup").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/*/members").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/*/auth/email/verification-code").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/*/auth/email/verify").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/*/auth/find-account").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/*/auth/password/reset").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/*/auth/password/reset/verify").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/api/*/auth/password/reset/new").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/session").permitAll()
                .requestMatchers("/oauth2/**").permitAll()
                .requestMatchers("/login/oauth2/**").permitAll()
                .requestMatchers("/login/**").permitAll()
                    // v2 회원가입
                .requestMatchers(HttpMethod.POST, "/api/*/auth/sign-up").permitAll()
                .anyRequest().authenticated()
        )
        .headers(headers ->
            headers.frameOptions(frameOptions ->
                frameOptions.sameOrigin()
            )
        )
        .csrf(csrf -> csrf.disable())
        .httpBasic(httpBasic -> httpBasic.disable())
        .formLogin(
            AbstractHttpConfigurer::disable
        )
        .sessionManagement(sessionManagement ->
            sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS
            )
        )
        .oauth2Login(
            oauth2Login -> oauth2Login
                .successHandler(customOAuth2AuthenticationSuccessHandler)
                .authorizationEndpoint(
                    authorizationEndpoint -> authorizationEndpoint
                        .authorizationRequestResolver(customAuthorizationRequestResolver)
                )
        )
        .addFilterBefore(
            customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class
        );

    // CORS 설정 적용
    http.addFilterBefore(new CorsFilter(corsConfigurationSource()), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
