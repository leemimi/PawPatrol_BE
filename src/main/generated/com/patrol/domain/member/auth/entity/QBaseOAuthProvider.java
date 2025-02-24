package com.patrol.domain.member.auth.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseOAuthProvider is a Querydsl query type for BaseOAuthProvider
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QBaseOAuthProvider extends EntityPathBase<BaseOAuthProvider> {

    private static final long serialVersionUID = -1049861613L;

    public static final QBaseOAuthProvider baseOAuthProvider = new QBaseOAuthProvider("baseOAuthProvider");

    public final StringPath providerId = createString("providerId");

    public QBaseOAuthProvider(String variable) {
        super(BaseOAuthProvider.class, forVariable(variable));
    }

    public QBaseOAuthProvider(Path<? extends BaseOAuthProvider> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseOAuthProvider(PathMetadata metadata) {
        super(BaseOAuthProvider.class, metadata);
    }

}

