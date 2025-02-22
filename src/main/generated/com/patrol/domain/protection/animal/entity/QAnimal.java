package com.patrol.domain.protection.animal.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAnimal is a Querydsl query type for Animal
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAnimal extends EntityPathBase<Animal> {

    private static final long serialVersionUID = 271057173L;

    public static final QAnimal animal = new QAnimal("animal");

    public final com.patrol.global.jpa.QBaseAnimalEntity _super = new com.patrol.global.jpa.QBaseAnimalEntity(this);

    //inherited
    public final StringPath breed = _super.breed;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath estimatedAge = createString("estimatedAge");

    //inherited
    public final StringPath feature = _super.feature;

    //inherited
    public final EnumPath<com.patrol.domain.protection.animal.enums.AnimalGender> gender = _super.gender;

    //inherited
    public final StringPath healthCondition = _super.healthCondition;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    //inherited
    public final EnumPath<com.patrol.domain.protection.animal.enums.AnimalSize> size = _super.size;

    public QAnimal(String variable) {
        super(Animal.class, forVariable(variable));
    }

    public QAnimal(Path<? extends Animal> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAnimal(PathMetadata metadata) {
        super(Animal.class, metadata);
    }

}

