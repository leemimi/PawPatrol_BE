package com.patrol.domain.member.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1890750928L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final com.patrol.global.jpa.QBaseEntity _super = new com.patrol.global.jpa.QBaseEntity(this);

    public final StringPath address = createString("address");

    public final StringPath apiKey = createString("apiKey");

    public final DatePath<java.time.LocalDate> birthDate = createDate("birthDate", java.time.LocalDate.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final EnumPath<com.patrol.domain.member.member.enums.Gender> gender = createEnum("gender", com.patrol.domain.member.member.enums.Gender.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final EnumPath<com.patrol.domain.member.member.enums.ProviderType> loginType = createEnum("loginType", com.patrol.domain.member.member.enums.ProviderType.class);

    public final BooleanPath marketingAgree = createBoolean("marketingAgree");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath nickname = createString("nickname");

    public final com.patrol.domain.member.auth.entity.QOAuthProvider oAuthProvider;

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final StringPath profileImageUrl = createString("profileImageUrl");

    public final EnumPath<com.patrol.domain.member.member.enums.MemberRole> role = createEnum("role", com.patrol.domain.member.member.enums.MemberRole.class);

    public final EnumPath<com.patrol.domain.member.member.enums.MemberStatus> status = createEnum("status", com.patrol.domain.member.member.enums.MemberStatus.class);

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.oAuthProvider = inits.isInitialized("oAuthProvider") ? new com.patrol.domain.member.auth.entity.QOAuthProvider(forProperty("oAuthProvider"), inits.get("oAuthProvider")) : null;
    }

}

