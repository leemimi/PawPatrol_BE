package com.patrol.global.rq;



import com.patrol.domain.member.auth.service.V2AuthService;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.enums.MemberRole;
import com.patrol.global.security.SecurityUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Arrays;
import java.util.Optional;

@RequestScope
@Component
@RequiredArgsConstructor
public class Rq {
  private final HttpServletRequest req;
  private final HttpServletResponse resp;
  private final V2AuthService authService;

  @Value("${custom.cookie.domain}")
  private String domain;


  public void setLogin(Member member) {
    UserDetails user = new SecurityUser(
        member.getId(),
        member.getEmail(),
        "",
        member.getNickname(),
        member.getProfileImageUrl(),
        member.getAuthorities()
    );

    Authentication authentication = new UsernamePasswordAuthenticationToken(
        user,
        user.getPassword(),
        user.getAuthorities()
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  public Member getActor() {
    return Optional.ofNullable(
            SecurityContextHolder
                .getContext()
                .getAuthentication()
        )
        .map(Authentication::getPrincipal)
        .filter(principal -> principal instanceof SecurityUser)
        .map(principal -> (SecurityUser) principal)
        .map(securityUser -> {
          String roleName = securityUser.getAuthorities()
              .stream()
              .findFirst()
              .map(GrantedAuthority::getAuthority)
              .orElse(MemberRole.ROLE_USER.name());
          MemberRole role = MemberRole.valueOf(roleName);

          return new Member(
              securityUser.getId(),
              securityUser.getUsername(),
              securityUser.getNickname(),
              securityUser.getProfileImageUrl(),
              role
          );
        })
        .orElse(null);
  }

  // accessToken 쿠키 재발급
  public void setCookie(String name, String value) {
    ResponseCookie cookie = ResponseCookie.from(name, value)
        .path("/")
        .domain(domain)
        .sameSite("Strict")
        .secure(true)
        .httpOnly(true)
        .build();
    resp.addHeader("Set-Cookie", cookie.toString());
  }

  // 키에서 지정된 이름(name)의 값을 조회, accessToken || apiKey
  public String getCookieValue(String name) {
    return Optional
        .ofNullable(req.getCookies())
        .stream() // 1 ~ 0
        .flatMap(cookies -> Arrays.stream(cookies))
        .filter(cookie -> cookie.getName().equals(name))
        .map(cookie -> cookie.getValue())
        .findFirst()
        .orElse(null);
  }


  public void deleteCookie(String name) {
    ResponseCookie cookie = ResponseCookie.from(name, null)
        .path("/")
        .domain(domain)
        .sameSite("Strict")
        .secure(true)
        .httpOnly(true)
        .maxAge(0)
        .build();
    resp.addHeader("Set-Cookie", cookie.toString());
  }

  public String getHeader(String name) {
    return req.getHeader(name);
  }

  // 사용자 인증 정보(apiKey, accessToken)를 쿠키에 저장하고 accessToken 반환
  public String makeAuthCookies(Member member) {
    String accessToken = authService.genAccessToken(member);
    setCookie("apiKey", member.getApiKey());
    setCookie("accessToken", accessToken);
    return accessToken;
  }
}
