package com.patrol.domain.animalCase.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAnimalCase is a Querydsl query type for AnimalCase
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAnimalCase extends EntityPathBase<AnimalCase> {

    private static final long serialVersionUID = 1288441098L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAnimalCase animalCase = new QAnimalCase("animalCase");

    public final com.patrol.global.jpa.QBaseEntity _super = new com.patrol.global.jpa.QBaseEntity(this);

    public final com.patrol.domain.animal.entity.QAnimal animal;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.patrol.domain.member.member.entity.QMember currentFoster;

    public final ListPath<CaseHistory, QCaseHistory> histories = this.<CaseHistory, QCaseHistory>createList("histories", CaseHistory.class, QCaseHistory.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final ListPath<com.patrol.domain.protection.entity.Protection, com.patrol.domain.protection.entity.QProtection> protections = this.<com.patrol.domain.protection.entity.Protection, com.patrol.domain.protection.entity.QProtection>createList("protections", com.patrol.domain.protection.entity.Protection.class, com.patrol.domain.protection.entity.QProtection.class, PathInits.DIRECT2);

    public final EnumPath<com.patrol.domain.animalCase.enums.CaseStatus> status = createEnum("status", com.patrol.domain.animalCase.enums.CaseStatus.class);

    public QAnimalCase(String variable) {
        this(AnimalCase.class, forVariable(variable), INITS);
    }

    public QAnimalCase(Path<? extends AnimalCase> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAnimalCase(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAnimalCase(PathMetadata metadata, PathInits inits) {
        this(AnimalCase.class, metadata, inits);
    }

    public QAnimalCase(Class<? extends AnimalCase> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.animal = inits.isInitialized("animal") ? new com.patrol.domain.animal.entity.QAnimal(forProperty("animal"), inits.get("animal")) : null;
        this.currentFoster = inits.isInitialized("currentFoster") ? new com.patrol.domain.member.member.entity.QMember(forProperty("currentFoster"), inits.get("currentFoster")) : null;
    }

}

