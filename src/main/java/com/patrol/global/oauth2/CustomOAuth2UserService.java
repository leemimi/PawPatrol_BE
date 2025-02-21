package com.patrol.global.oauth2;



import com.patrol.domain.member.auth.service.AuthService;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.enums.ProviderType;
import com.patrol.domain.member.member.service.MemberService;
import com.patrol.domain.member.member.service.SocialConnectService;
import com.patrol.global.exceptions.ErrorCodes;
import com.patrol.global.exceptions.ServiceException;
import com.patrol.global.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Locale;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final AuthService authService;
  private final MemberService memberService;
  private final SocialConnectService socialConnectService;

  @Transactional
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) {
    OAuth2User oAuth2User = super.loadUser(userRequest);

    String oauthId = oAuth2User.getName();  // OAuth 로그인 시 Id값 가져오기 (pk)
    
    // 유저가 어떻게 로그인 했는지 (일반, 구글, 카카오 등) 여기서 알 수 있음
    ProviderType loginType = ProviderType.of(userRequest
        .getClientRegistration()
        .getRegistrationId()  // 반환 값 예시 : "google", "naver", "kakao"
        .toUpperCase(Locale.getDefault()));

    String email;
    String nickname;
    String profileImageUrl;

    switch (loginType) {
      case KAKAO -> {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, String> properties = (Map<String, String>) attributes.get("properties");
        nickname = properties.get("nickname");
        profileImageUrl = properties.get("profile_image");
        Map<String, String> kakaoAccount = (Map<String, String>) attributes.get("kakao_account");
        email = kakaoAccount.get("email");
      }

      case GOOGLE -> {
        email = oAuth2User.getAttribute("email");
        nickname = oAuth2User.getAttribute("name");
        profileImageUrl = oAuth2User.getAttribute("picture");
      }

      case NAVER -> {
        Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttributes().get("response");
        email = (String) response.get("email");
        nickname = (String) response.get("nickname");
        profileImageUrl = (String) response.get("profile_image");
        oauthId = (String) response.get("id");
      }

      default -> throw new ServiceException(ErrorCodes.INVALID_LOGIN_TYPE);
    }

    String providerId = loginType + "__" + oauthId;   // ex) KAKAO__1234567890, 서비스 구분자와 oauthId를 합쳐서 고유한 ID값 생성

    Long originMemberId = socialConnectService.getOrigin();

    // 사이트 자체 계정이 존재할 경우 isConnection = true
    boolean isConnection = originMemberId != null;

    if (isConnection) { // 사이트 자체 계정이 존재
      Member loginUser = memberService.findById(originMemberId)
          .orElseThrow(() -> new ServiceException(ErrorCodes.MEMBER_NOT_FOUND));

      authService.connectOAuthProvider(loginUser, loginType, providerId, email);
      // Redis 서버에 멤버 정보가 남아있을 필요가 없으므로 삭제 (토큰으로 저장됨)
      socialConnectService.removeOrigin();

      return new SecurityUser(
          loginUser.getId(),
          loginUser.getEmail(),
          loginUser.getPassword(),
          loginUser.getNickname(),
          loginUser.getProfileImageUrl(),
          loginUser.getAuthorities()
      );

    } else {  // 사이트 자체 계정이 존재 X

      Member member = authService.handleSocialLogin(
          email, nickname, loginType, profileImageUrl, providerId
      );

      return new SecurityUser(
          member.getId(),
          member.getEmail(),
          member.getPassword(),
          member.getNickname(),
          member.getProfileImageUrl(),
          member.getAuthorities()
      );
    }
  }


//  private String _getGithubPrivateEmail(OAuth2AccessToken accessToken) {
//    try {
//      RestTemplate restTemplate = new RestTemplate();
//      HttpHeaders headers = new HttpHeaders();
//      headers.setBearerAuth(accessToken.getTokenValue());
//
//      HttpEntity<?> entity = new HttpEntity<>(headers);
//      ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
//          "https://api.github.com/user/emails",
//          HttpMethod.GET,
//          entity,
//          new ParameterizedTypeReference<List<Map<String, Object>>>() {}
//      );
//
//      return response.getBody().stream()
//          .filter(emailObj -> (Boolean) emailObj.get("primary") && (Boolean) emailObj.get("verified"))
//          .map(emailObj -> (String) emailObj.get("email"))
//          .findFirst()
//          .orElse(null);
//
//    } catch (Exception e) {
//      return null;
//    }
//  }
}
