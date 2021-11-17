package kr.co.mcedu.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserAlarmEntity is a Querydsl query type for UserAlarmEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserAlarmEntity extends EntityPathBase<UserAlarmEntity> {

    private static final long serialVersionUID = -1181402440L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserAlarmEntity userAlarmEntity = new QUserAlarmEntity("userAlarmEntity");

    public final kr.co.mcedu.common.entity.QBaseTimeEntity _super = new kr.co.mcedu.common.entity.QBaseTimeEntity(this);

    public final NumberPath<Long> alarmSeq = createNumber("alarmSeq", Long.class);

    public final EnumPath<kr.co.mcedu.user.model.UserAlarmType> alarmType = createEnum("alarmType", kr.co.mcedu.user.model.UserAlarmType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final BooleanPath isDelete = createBoolean("isDelete");

    public final BooleanPath isRead = createBoolean("isRead");

    public final StringPath landingUrl = createString("landingUrl");

    public final StringPath message = createString("message");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final QWebUserEntity webUserEntity;

    public QUserAlarmEntity(String variable) {
        this(UserAlarmEntity.class, forVariable(variable), INITS);
    }

    public QUserAlarmEntity(Path<? extends UserAlarmEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserAlarmEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserAlarmEntity(PathMetadata metadata, PathInits inits) {
        this(UserAlarmEntity.class, metadata, inits);
    }

    public QUserAlarmEntity(Class<? extends UserAlarmEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.webUserEntity = inits.isInitialized("webUserEntity") ? new QWebUserEntity(forProperty("webUserEntity")) : null;
    }

}

