package com.patrol.domain.member.auth.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QKakaoProvider is a Querydsl query type for KakaoProvider
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QKakaoProvider extends BeanPath<KakaoProvider> {

    private static final long serialVersionUID = 1432962416L;

    public static final QKakaoProvider kakaoProvider = new QKakaoProvider("kakaoProvider");

    public final QBaseOAuthProvider _super = new QBaseOAuthProvider(this);

    public final BooleanPath connected = createBoolean("connected");

    public final DateTimePath<java.time.LocalDateTime> connectedAt = createDateTime("connectedAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final DateTimePath<java.time.LocalDateTime> modifiedAt = createDateTime("modifiedAt", java.time.LocalDateTime.class);

    public final StringPath providerId = createString("providerId");

    public QKakaoProvider(String variable) {
        super(KakaoProvider.class, forVariable(variable));
    }

    public QKakaoProvider(Path<? extends KakaoProvider> path) {
        super(path.getType(), path.getMetadata());
    }

    public QKakaoProvider(PathMetadata metadata) {
        super(KakaoProvider.class, metadata);
    }

}

