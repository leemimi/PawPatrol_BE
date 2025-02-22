package com.patrol.domain.protection.animalCase.entity;

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

    private static final long serialVersionUID = 603896629L;

    public static final QAnimalCase animalCase = new QAnimalCase("animalCase");

    public final com.patrol.global.jpa.QBaseEntity _super = new com.patrol.global.jpa.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ListPath<CaseHistory, QCaseHistory> histories = this.<CaseHistory, QCaseHistory>createList("histories", CaseHistory.class, QCaseHistory.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final EnumPath<com.patrol.domain.protection.animalCase.enums.CaseStatus> status = createEnum("status", com.patrol.domain.protection.animalCase.enums.CaseStatus.class);

    public final NumberPath<Long> targetId = createNumber("targetId", Long.class);

    public final EnumPath<com.patrol.domain.protection.animalCase.enums.TargetType> targetType = createEnum("targetType", com.patrol.domain.protection.animalCase.enums.TargetType.class);

    public QAnimalCase(String variable) {
        super(AnimalCase.class, forVariable(variable));
    }

    public QAnimalCase(Path<? extends AnimalCase> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAnimalCase(PathMetadata metadata) {
        super(AnimalCase.class, metadata);
    }

}

