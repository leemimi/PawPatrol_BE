package com.patrol.domain.member.auth.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOAuthProvider is a Querydsl query type for OAuthProvider
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOAuthProvider extends EntityPathBase<OAuthProvider> {

    private static final long serialVersionUID = -37483676L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOAuthProvider oAuthProvider = new QOAuthProvider("oAuthProvider");

    public final com.patrol.global.jpa.QBaseEntity _super = new com.patrol.global.jpa.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QGoogleProvider google;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final QKakaoProvider kakao;

    public final com.patrol.domain.member.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final QNaverProvider naver;

    public QOAuthProvider(String variable) {
        this(OAuthProvider.class, forVariable(variable), INITS);
    }

    public QOAuthProvider(Path<? extends OAuthProvider> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOAuthProvider(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOAuthProvider(PathMetadata metadata, PathInits inits) {
        this(OAuthProvider.class, metadata, inits);
    }

    public QOAuthProvider(Class<? extends OAuthProvider> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.google = inits.isInitialized("google") ? new QGoogleProvider(forProperty("google")) : null;
        this.kakao = inits.isInitialized("kakao") ? new QKakaoProvider(forProperty("kakao")) : null;
        this.member = inits.isInitialized("member") ? new com.patrol.domain.member.member.entity.QMember(forProperty("member"), inits.get("member")) : null;
        this.naver = inits.isInitialized("naver") ? new QNaverProvider(forProperty("naver")) : null;
    }

}

