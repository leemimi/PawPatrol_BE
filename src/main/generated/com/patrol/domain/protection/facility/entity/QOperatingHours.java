package com.patrol.domain.protection.facility.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOperatingHours is a Querydsl query type for OperatingHours
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QOperatingHours extends BeanPath<OperatingHours> {

    private static final long serialVersionUID = -1003518514L;

    public static final QOperatingHours operatingHours = new QOperatingHours("operatingHours");

    public final StringPath closedDays = createString("closedDays");

    public final StringPath weekdayTime = createString("weekdayTime");

    public final StringPath weekendTime = createString("weekendTime");

    public QOperatingHours(String variable) {
        super(OperatingHours.class, forVariable(variable));
    }

    public QOperatingHours(Path<? extends OperatingHours> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOperatingHours(PathMetadata metadata) {
        super(OperatingHours.class, metadata);
    }

}

