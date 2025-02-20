package com.patrol.domain.member.auth.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QNaverProvider is a Querydsl query type for NaverProvider
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QNaverProvider extends BeanPath<NaverProvider> {

    private static final long serialVersionUID = 347674813L;

    public static final QNaverProvider naverProvider = new QNaverProvider("naverProvider");

    public final QBaseOAuthProvider _super = new QBaseOAuthProvider(this);

    public final BooleanPath connected = createBoolean("connected");

    public final DateTimePath<java.time.LocalDateTime> connectedAt = createDateTime("connectedAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final DateTimePath<java.time.LocalDateTime> modifiedAt = createDateTime("modifiedAt", java.time.LocalDateTime.class);

    public final StringPath providerId = createString("providerId");

    public QNaverProvider(String variable) {
        super(NaverProvider.class, forVariable(variable));
    }

    public QNaverProvider(Path<? extends NaverProvider> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNaverProvider(PathMetadata metadata) {
        super(NaverProvider.class, metadata);
    }

}

