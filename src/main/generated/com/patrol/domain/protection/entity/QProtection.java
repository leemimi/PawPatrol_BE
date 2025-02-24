package com.patrol.domain.protection.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProtection is a Querydsl query type for Protection
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProtection extends EntityPathBase<Protection> {

    private static final long serialVersionUID = -1791681820L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProtection protection = new QProtection("protection");

    public final com.patrol.global.jpa.QBaseEntity _super = new com.patrol.global.jpa.QBaseEntity(this);

    public final com.patrol.domain.animalCase.entity.QAnimalCase animalCase;

    public final com.patrol.domain.member.member.entity.QMember applicant;

    public final DateTimePath<java.time.LocalDateTime> approvedDate = createDateTime("approvedDate", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final EnumPath<com.patrol.domain.protection.enums.ProtectionStatus> protectionStatus = createEnum("protectionStatus", com.patrol.domain.protection.enums.ProtectionStatus.class);

    public final StringPath reason = createString("reason");

    public final StringPath rejectReason = createString("rejectReason");

    public QProtection(String variable) {
        this(Protection.class, forVariable(variable), INITS);
    }

    public QProtection(Path<? extends Protection> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProtection(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProtection(PathMetadata metadata, PathInits inits) {
        this(Protection.class, metadata, inits);
    }

    public QProtection(Class<? extends Protection> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.animalCase = inits.isInitialized("animalCase") ? new com.patrol.domain.animalCase.entity.QAnimalCase(forProperty("animalCase"), inits.get("animalCase")) : null;
        this.applicant = inits.isInitialized("applicant") ? new com.patrol.domain.member.member.entity.QMember(forProperty("applicant"), inits.get("applicant")) : null;
    }

}

