package com.patrol.domain.facility.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHospital is a Querydsl query type for Hospital
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHospital extends EntityPathBase<Hospital> {

    private static final long serialVersionUID = -1169127921L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHospital hospital = new QHospital("hospital");

    public final QFacility _super;

    //inherited
    public final StringPath address;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt;

    public final StringPath homepage = createString("homepage");

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

    //inherited
    public final StringPath tel;

    public QHospital(String variable) {
        this(Hospital.class, forVariable(variable), INITS);
    }

    public QHospital(Path<? extends Hospital> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHospital(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHospital(PathMetadata metadata, PathInits inits) {
        this(Hospital.class, metadata, inits);
    }

    public QHospital(Class<? extends Hospital> type, PathMetadata metadata, PathInits inits) {
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

