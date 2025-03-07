package com.patrol.global.security;



import com.patrol.domain.member.auth.service.AuthService;
import com.patrol.domain.member.auth.service.V2AuthService;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.rq.Rq;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {
  private final AuthService authService;
  private final V2AuthService v2AuthService;
  private final Rq rq;
  private final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFilter.class.getName());
  record AuthTokens(String apiKey, String accessToken) {
  }


  private AuthTokens _getAuthTokensFromRequest() {
    String authorization = rq.getHeader("Authorization");

    // 헤더로 토큰 정보 받았을 경우
    if (authorization != null && authorization.startsWith("Bearer ")) {
      String token = authorization.substring("Bearer ".length());
      String[] tokenBits = token.split(" ", 2);
      if (tokenBits.length == 2) {
        return new AuthTokens(tokenBits[0], tokenBits[1]);
      }
    }

    // 쿠키로 토큰 정보 받았을 경우
    String apiKey = rq.getCookieValue("apiKey");
    logger.info("_getAuthTokensFromRequest: " + apiKey);
    String accessToken = rq.getCookieValue("accessToken");
    logger.info("_getAuthTokensFromRequest: " + accessToken);
    if (apiKey != null && accessToken != null) {
      return new AuthTokens(apiKey, accessToken);
    }
    return null;
  }


  // 쿠키 시간 만료 되었을 때 재발급
  private void _refreshAccessToken(Member member) {
    logger.info("access 쿠키 재발급 _refreshAccessTokenByApiKey");
    String newAccessToken = authService.genAccessToken(member);
    // 클라이언트에서 Header에 담에 서버로 보냄, 서버에서는 getHeader만 필요 setHeaderX
//    rq.setHeader("Authorization", "Bearer " + member.getApiKey() + " " + newAccessToken);
    rq.setCookie("accessToken", newAccessToken);
  }


  private Member _refreshAccessTokenByApiKey(String apiKey) {
    logger.info("access 토큰 재발급 _refreshAccessTokenByApiKey");
    Optional<Member> opMemberByApiKey = v2AuthService.findByApiKey(apiKey);
    if (opMemberByApiKey.isEmpty()) {
      logger.info("access 토큰 재발급 opMemberByApiKey.isEmpty()");
      return null;
    }
    Member member = opMemberByApiKey.get();
    _refreshAccessToken(member);
    return member;
  }


  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    if (!request.getRequestURI().startsWith("/api/") ||
            request.getRequestURI().startsWith("/api/placeholder/")) {  // 소셜 로그인 플레이스홀더 이미지 경로 추가
      filterChain.doFilter(request, response);
      return;
    }

    if (List.of(
        "/api/v1/auth/login", "/api/v1/auth/logout",
        "/api/v1/auth/signup",
        "/api/v1/auth/email/verification-code", "/api/v1/auth/email/verify",
        "/api/v1/auth/find-account",
        "/api/v1/auth/password/reset", "/api/v1/auth/password/reset/verify",
        "/api/v1/auth/password/reset/new",
            "/api/v2/auth/login", "/api/v2/auth/logout",
            "/api/v2/auth/signup"
        ).contains(request.getRequestURI())
    ) {
      filterChain.doFilter(request, response);
      return;
    }

    AuthTokens authTokens = _getAuthTokensFromRequest();
    if (authTokens == null) {
      filterChain.doFilter(request, response);
      return;
    }

    String apiKey = authTokens.apiKey;
    String accessToken = authTokens.accessToken;

    Member member = authService.getMemberFromAccessToken(accessToken);
    if (member == null) { // 토큰이 만료되었을 때, 당연히 멤버 정보 못가져옴
      logger.info("토큰 만료됨, doFilterInternal");
      member = _refreshAccessTokenByApiKey(apiKey);
    }
    if (member != null) { // 토큰이 만료되지 않았을 때
      rq.setLogin(member);
    }
    filterChain.doFilter(request, response);
  }
}
