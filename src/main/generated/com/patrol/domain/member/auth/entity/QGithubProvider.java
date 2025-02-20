package com.patrol.domain.member.auth.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGithubProvider is a Querydsl query type for GithubProvider
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QGithubProvider extends BeanPath<GithubProvider> {

    private static final long serialVersionUID = -1980047144L;

    public static final QGithubProvider githubProvider = new QGithubProvider("githubProvider");

    public final QBaseOAuthProvider _super = new QBaseOAuthProvider(this);

    public final BooleanPath connected = createBoolean("connected");

    public final DateTimePath<java.time.LocalDateTime> connectedAt = createDateTime("connectedAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final DateTimePath<java.time.LocalDateTime> modifiedAt = createDateTime("modifiedAt", java.time.LocalDateTime.class);

    public final StringPath providerId = createString("providerId");

    public QGithubProvider(String variable) {
        super(GithubProvider.class, forVariable(variable));
    }

    public QGithubProvider(Path<? extends GithubProvider> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGithubProvider(PathMetadata metadata) {
        super(GithubProvider.class, metadata);
    }

}

