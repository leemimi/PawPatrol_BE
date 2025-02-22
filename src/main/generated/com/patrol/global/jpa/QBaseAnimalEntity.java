package com.patrol.global.jpa;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseAnimalEntity is a Querydsl query type for BaseAnimalEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QBaseAnimalEntity extends EntityPathBase<BaseAnimalEntity> {

    private static final long serialVersionUID = 1626197145L;

    public static final QBaseAnimalEntity baseAnimalEntity = new QBaseAnimalEntity("baseAnimalEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath breed = createString("breed");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath feature = createString("feature");

    public final EnumPath<com.patrol.domain.protection.animal.enums.AnimalGender> gender = createEnum("gender", com.patrol.domain.protection.animal.enums.AnimalGender.class);

    public final StringPath healthCondition = createString("healthCondition");

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final EnumPath<com.patrol.domain.protection.animal.enums.AnimalSize> size = createEnum("size", com.patrol.domain.protection.animal.enums.AnimalSize.class);

    public QBaseAnimalEntity(String variable) {
        super(BaseAnimalEntity.class, forVariable(variable));
    }

    public QBaseAnimalEntity(Path<? extends BaseAnimalEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseAnimalEntity(PathMetadata metadata) {
        super(BaseAnimalEntity.class, metadata);
    }

}

