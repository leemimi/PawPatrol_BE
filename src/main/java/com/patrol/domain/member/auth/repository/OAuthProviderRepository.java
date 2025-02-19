package com.patrol.domain.member.auth.repository;


import com.patrol.domain.member.auth.entity.OAuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthProviderRepository extends JpaRepository<OAuthProvider, Long> {

}
