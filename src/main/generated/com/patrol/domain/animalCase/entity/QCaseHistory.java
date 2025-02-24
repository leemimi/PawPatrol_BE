package com.patrol.domain.animalCase.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCaseHistory is a Querydsl query type for CaseHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCaseHistory extends EntityPathBase<CaseHistory> {

    private static final long serialVersionUID = 1836700998L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCaseHistory caseHistory = new QCaseHistory("caseHistory");

    public final com.patrol.global.jpa.QBaseEntity _super = new com.patrol.global.jpa.QBaseEntity(this);

    public final QAnimalCase animalCase;

    public final NumberPath<Long> contentId = createNumber("contentId", Long.class);

    public final EnumPath<com.patrol.domain.animalCase.enums.ContentType> contentType = createEnum("contentType", com.patrol.domain.animalCase.enums.ContentType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<com.patrol.domain.animalCase.enums.CaseHistoryStatus> historyStatus = createEnum("historyStatus", com.patrol.domain.animalCase.enums.CaseHistoryStatus.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public QCaseHistory(String variable) {
        this(CaseHistory.class, forVariable(variable), INITS);
    }

    public QCaseHistory(Path<? extends CaseHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCaseHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCaseHistory(PathMetadata metadata, PathInits inits) {
        this(CaseHistory.class, metadata, inits);
    }

    public QCaseHistory(Class<? extends CaseHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.animalCase = inits.isInitialized("animalCase") ? new QAnimalCase(forProperty("animalCase"), inits.get("animalCase")) : null;
    }

}

