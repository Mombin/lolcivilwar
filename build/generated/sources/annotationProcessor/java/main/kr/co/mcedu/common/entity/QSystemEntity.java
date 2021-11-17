package kr.co.mcedu.common.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSystemEntity is a Querydsl query type for SystemEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSystemEntity extends EntityPathBase<SystemEntity> {

    private static final long serialVersionUID = 496709443L;

    public static final QSystemEntity systemEntity = new QSystemEntity("systemEntity");

    public final StringPath propertyName = createString("propertyName");

    public final StringPath propertyValue1 = createString("propertyValue1");

    public final StringPath propertyValue2 = createString("propertyValue2");

    public QSystemEntity(String variable) {
        super(SystemEntity.class, forVariable(variable));
    }

    public QSystemEntity(Path<? extends SystemEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSystemEntity(PathMetadata metadata) {
        super(SystemEntity.class, metadata);
    }

}

