package com.patrol.domain.protection.facility.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QShelter is a Querydsl query type for Shelter
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShelter extends EntityPathBase<Shelter> {

    private static final long serialVersionUID = 1609582949L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QShelter shelter = new QShelter("shelter");

    public final QFacility _super;

    //inherited
    public final StringPath address;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt;

    //inherited
    public final NumberPath<Long> id;

    //inherited
    public final NumberPath<Double> latitude;

    //inherited
    public final NumberPath<Double> longitude;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt;

    //inherited
    public final StringPath name;

    // inherited
    public final QOperatingHours operatingHours;

    public final StringPath saveTargetAnimal = createString("saveTargetAnimal");

    //inherited
    public final StringPath tel;

    public final NumberPath<Integer> vetPersonCount = createNumber("vetPersonCount", Integer.class);

    public QShelter(String variable) {
        this(Shelter.class, forVariable(variable), INITS);
    }

    public QShelter(Path<? extends Shelter> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QShelter(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QShelter(PathMetadata metadata, PathInits inits) {
        this(Shelter.class, metadata, inits);
    }

    public QShelter(Class<? extends Shelter> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QFacility(type, metadata, inits);
        this.address = _super.address;
        this.createdAt = _super.createdAt;
        this.id = _super.id;
        this.latitude = _super.latitude;
        this.longitude = _super.longitude;
        this.modifiedAt = _super.modifiedAt;
        this.name = _super.name;
        this.operatingHours = _super.operatingHours;
        this.tel = _super.tel;
    }

}

