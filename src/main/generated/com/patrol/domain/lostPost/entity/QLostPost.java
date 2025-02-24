package com.patrol.domain.LostPost.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLostPost is a Querydsl query type for LostPost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLostPost extends EntityPathBase<LostPost> {

    private static final long serialVersionUID = -404365542L;

    public static final QLostPost lostPost = new QLostPost("lostPost");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final StringPath location = createString("location");

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final NumberPath<Long> lostId = createNumber("lostId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lostTime = createDateTime("lostTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> modifiedAt = createDateTime("modifiedAt", java.time.LocalDateTime.class);

    public final StringPath ownerPhone = createString("ownerPhone");

    public final NumberPath<Long> petId = createNumber("petId", Long.class);

    public final EnumPath<LostPost.Status> status = createEnum("status", LostPost.Status.class);

    public final StringPath tags = createString("tags");

    public final StringPath title = createString("title");

    public QLostPost(String variable) {
        super(LostPost.class, forVariable(variable));
    }

    public QLostPost(Path<? extends LostPost> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLostPost(PathMetadata metadata) {
        super(LostPost.class, metadata);
    }

}

