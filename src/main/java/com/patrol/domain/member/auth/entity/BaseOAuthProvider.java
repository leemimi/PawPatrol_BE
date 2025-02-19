package com.patrol.domain.member.auth.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseOAuthProvider {

  protected abstract String getProviderId();
  protected abstract void setConnected(boolean connected);
  protected abstract void setConnectedAt(LocalDateTime connectedAt);
  protected abstract void setModifiedAt(LocalDateTime modifiedAt);

  protected void connect() {
    setConnected(true);
    setConnectedAt(LocalDateTime.now());
    setModifiedAt(LocalDateTime.now());
  }

  protected void disconnect() {
    setConnected(false);
    setModifiedAt(LocalDateTime.now());
  }

  protected void validate() {
    if (getProviderId() == null || getProviderId().isBlank()) {
      throw new IllegalArgumentException("Provider ID가 유효하지 않습니다.");
    }
  }
}
