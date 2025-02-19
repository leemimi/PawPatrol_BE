package com.patrol.domain.member.auth.strategy;


import com.patrol.domain.member.auth.entity.OAuthProvider;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.enums.ProviderType;
import com.patrol.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GithubProviderStrategy implements OAuthProviderStrategy {

  private final MemberRepository memberRepository;

  @Override
  public ProviderType getProviderType() {
    return ProviderType.GITHUB;
  }

  @Override
  public Member findByProviderId(String providerId) {
    return memberRepository.findByGithubProviderId(providerId).orElse(null);
  }

  @Override
  public void connect(Member member, String providerId, String email) {
    OAuthProvider oAuthProvider = member.getOAuthProviderOrCreate();
    oAuthProvider.addGithubProvider(providerId, email);
  }

  @Override
  public void disconnect(Member member) {
    OAuthProvider oAuthProvider = member.getOAuthProviderOrCreate();
    oAuthProvider.removeGithubProvider();
  }
}
