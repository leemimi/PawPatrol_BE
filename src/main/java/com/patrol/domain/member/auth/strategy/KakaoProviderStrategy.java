package com.patrol.domain.member.auth.strategy;


import com.patrol.domain.member.auth.entity.OAuthProvider;
import com.patrol.domain.member.auth.repository.OAuthProviderRepository;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.enums.ProviderType;
import com.patrol.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoProviderStrategy implements OAuthProviderStrategy {

  private final MemberRepository memberRepository;
  private final OAuthProviderRepository oAuthProviderRepository;
  @Override
  public ProviderType getProviderType() {
    return ProviderType.KAKAO;
  }

  @Override
  public Member findByProviderId(String providerId) {
    return memberRepository.findByKakaoProviderId(providerId).orElse(null);
  }

  @Override
  public void connect(Member member, String providerId, String email) {
    OAuthProvider oAuthProvider = member.getOAuthProviderOrCreate();
    oAuthProvider.addKakaoProvider(providerId, email);
  }

  @Override
  public void disconnect(Member member) {
    OAuthProvider oAuthProvider = member.getOAuthProvider();
    oAuthProvider.removeKakaoProvider();
    oAuthProviderRepository.save(oAuthProvider);
  }
}
