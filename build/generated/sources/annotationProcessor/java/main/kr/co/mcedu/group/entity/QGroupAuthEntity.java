package kr.co.mcedu.group.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGroupAuthEntity is a Querydsl query type for GroupAuthEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QGroupAuthEntity extends EntityPathBase<GroupAuthEntity> {

    private static final long serialVersionUID = 1590793719L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGroupAuthEntity groupAuthEntity = new QGroupAuthEntity("groupAuthEntity");

    public final kr.co.mcedu.common.entity.QBaseTimeEntity _super = new kr.co.mcedu.common.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QGroupEntity group;

    public final EnumPath<GroupAuthEnum> groupAuth = createEnum("groupAuth", GroupAuthEnum.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final kr.co.mcedu.user.entity.QWebUserEntity webUser;

    public QGroupAuthEntity(String variable) {
        this(GroupAuthEntity.class, forVariable(variable), INITS);
    }

    public QGroupAuthEntity(Path<? extends GroupAuthEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGroupAuthEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGroupAuthEntity(PathMetadata metadata, PathInits inits) {
        this(GroupAuthEntity.class, metadata, inits);
    }

    public QGroupAuthEntity(Class<? extends GroupAuthEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.group = inits.isInitialized("group") ? new QGroupEntity(forProperty("group")) : null;
        this.webUser = inits.isInitialized("webUser") ? new kr.co.mcedu.user.entity.QWebUserEntity(forProperty("webUser")) : null;
    }

}

