package kr.co.mcedu.match.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCustomMatchEntity is a Querydsl query type for CustomMatchEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCustomMatchEntity extends EntityPathBase<CustomMatchEntity> {

    private static final long serialVersionUID = 1288885374L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCustomMatchEntity customMatchEntity = new QCustomMatchEntity("customMatchEntity");

    public final kr.co.mcedu.common.entity.QBaseTimeEntity _super = new kr.co.mcedu.common.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final BooleanPath delYn = createBoolean("delYn");

    public final kr.co.mcedu.group.entity.QGroupEntity group;

    public final ListPath<MatchAttendeesEntity, QMatchAttendeesEntity> matchAttendees = this.<MatchAttendeesEntity, QMatchAttendeesEntity>createList("matchAttendees", MatchAttendeesEntity.class, QMatchAttendeesEntity.class, PathInits.DIRECT2);

    public final NumberPath<Long> matchSeq = createNumber("matchSeq", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public QCustomMatchEntity(String variable) {
        this(CustomMatchEntity.class, forVariable(variable), INITS);
    }

    public QCustomMatchEntity(Path<? extends CustomMatchEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCustomMatchEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCustomMatchEntity(PathMetadata metadata, PathInits inits) {
        this(CustomMatchEntity.class, metadata, inits);
    }

    public QCustomMatchEntity(Class<? extends CustomMatchEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.group = inits.isInitialized("group") ? new kr.co.mcedu.group.entity.QGroupEntity(forProperty("group")) : null;
    }

}

