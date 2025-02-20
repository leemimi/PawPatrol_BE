package com.patrol.domain.member.auth.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGoogleProvider is a Querydsl query type for GoogleProvider
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QGoogleProvider extends BeanPath<GoogleProvider> {

    private static final long serialVersionUID = -1947701970L;

    public static final QGoogleProvider googleProvider = new QGoogleProvider("googleProvider");

    public final QBaseOAuthProvider _super = new QBaseOAuthProvider(this);

    public final BooleanPath connected = createBoolean("connected");

    public final DateTimePath<java.time.LocalDateTime> connectedAt = createDateTime("connectedAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final DateTimePath<java.time.LocalDateTime> modifiedAt = createDateTime("modifiedAt", java.time.LocalDateTime.class);

    public final StringPath providerId = createString("providerId");

    public QGoogleProvider(String variable) {
        super(GoogleProvider.class, forVariable(variable));
    }

    public QGoogleProvider(Path<? extends GoogleProvider> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGoogleProvider(PathMetadata metadata) {
        super(GoogleProvider.class, metadata);
    }

}

