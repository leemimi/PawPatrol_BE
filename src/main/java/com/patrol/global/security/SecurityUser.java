package com.patrol.global.security;

import com.patrol.domain.member.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class SecurityUser extends User implements OAuth2User {
    @Getter
    private final long id;
    @Getter
    private final String nickname;
    @Getter
    private final String profileImageUrl;


    public SecurityUser(long id, String email, String password, String nickname, String profileImageUrl, Collection<? extends GrantedAuthority> authorities) {
        super(email, password != null ? password : "", authorities);
        this.id = id;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of(
                "id", id,
                "email", getUsername(),
                "nickname", nickname,
                "profileImageUrl", profileImageUrl
        );
    }

    @Override
    public String getName() {
        return getUsername();
    }
}

