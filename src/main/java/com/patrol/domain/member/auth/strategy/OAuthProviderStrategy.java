package com.patrol.domain.member.auth.strategy;



import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.enums.ProviderType;

public interface OAuthProviderStrategy {
  ProviderType getProviderType();
  Member findByProviderId(String providerId);
  void connect(Member member, String providerId, String email);
  void disconnect(Member member);
}
