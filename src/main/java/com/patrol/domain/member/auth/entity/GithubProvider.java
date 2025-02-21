//package com.patrol.domain.member.auth.entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Embeddable;
//import lombok.*;
//import lombok.experimental.SuperBuilder;
//
//import java.time.LocalDateTime;
//
//@Embeddable
//@Getter
//@SuperBuilder
//@AllArgsConstructor(access = AccessLevel.PROTECTED)
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@EqualsAndHashCode(of = "providerId", callSuper = false)
//public class GithubProvider extends BaseOAuthProvider {
//
//  @Column(name = "github_provider_id", length = 50)
//  private String providerId;
//
//  @Column(name = "github_email", length = 50)
//  private String email;
//
//  @Column(name = "github_connected")
//  private boolean connected = false;
//
//  @Column(name = "github_connected_at")
//  private LocalDateTime connectedAt;
//
//  @Column(name = "github_modified_at")
//  private LocalDateTime modifiedAt;
//
//
//  public void connect(String providerId, String email) {
//    this.providerId = providerId;
//    this.email = email;
//    super.connect();
//  }
//
//  @Override
//  protected void setConnected(boolean connected) {
//    this.connected = connected;
//  }
//
//  @Override
//  protected void setConnectedAt(LocalDateTime connectedAt) {
//    this.connectedAt = connectedAt;
//  }
//
//  @Override
//  protected void setModifiedAt(LocalDateTime modifiedAt) {
//    this.modifiedAt = modifiedAt;
//  }
//}
