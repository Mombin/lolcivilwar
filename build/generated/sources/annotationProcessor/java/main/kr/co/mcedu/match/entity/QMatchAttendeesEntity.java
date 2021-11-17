package kr.co.mcedu.match.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMatchAttendeesEntity is a Querydsl query type for MatchAttendeesEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMatchAttendeesEntity extends EntityPathBase<MatchAttendeesEntity> {

    private static final long serialVersionUID = 2128694192L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMatchAttendeesEntity matchAttendeesEntity = new QMatchAttendeesEntity("matchAttendeesEntity");

    public final kr.co.mcedu.common.entity.QBaseTimeEntity _super = new kr.co.mcedu.common.entity.QBaseTimeEntity(this);

    public final NumberPath<Long> attendeesSeq = createNumber("attendeesSeq", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QCustomMatchEntity customMatch;

    public final kr.co.mcedu.group.entity.QCustomUserEntity customUserEntity;

    public final BooleanPath delYn = createBoolean("delYn");

    public final BooleanPath matchResult = createBoolean("matchResult");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath position = createString("position");

    public final StringPath team = createString("team");

    public QMatchAttendeesEntity(String variable) {
        this(MatchAttendeesEntity.class, forVariable(variable), INITS);
    }

    public QMatchAttendeesEntity(Path<? extends MatchAttendeesEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMatchAttendeesEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMatchAttendeesEntity(PathMetadata metadata, PathInits inits) {
        this(MatchAttendeesEntity.class, metadata, inits);
    }

    public QMatchAttendeesEntity(Class<? extends MatchAttendeesEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.customMatch = inits.isInitialized("customMatch") ? new QCustomMatchEntity(forProperty("customMatch"), inits.get("customMatch")) : null;
        this.customUserEntity = inits.isInitialized("customUserEntity") ? new kr.co.mcedu.group.entity.QCustomUserEntity(forProperty("customUserEntity"), inits.get("customUserEntity")) : null;
    }

}

