package kr.co.mcedu.group.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGroupEntity is a Querydsl query type for GroupEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QGroupEntity extends EntityPathBase<GroupEntity> {

    private static final long serialVersionUID = 551071791L;

    public static final QGroupEntity groupEntity = new QGroupEntity("groupEntity");

    public final kr.co.mcedu.common.entity.QBaseTimeEntity _super = new kr.co.mcedu.common.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final ListPath<kr.co.mcedu.match.entity.CustomMatchEntity, kr.co.mcedu.match.entity.QCustomMatchEntity> customMatches = this.<kr.co.mcedu.match.entity.CustomMatchEntity, kr.co.mcedu.match.entity.QCustomMatchEntity>createList("customMatches", kr.co.mcedu.match.entity.CustomMatchEntity.class, kr.co.mcedu.match.entity.QCustomMatchEntity.class, PathInits.DIRECT2);

    public final ListPath<CustomUserEntity, QCustomUserEntity> customUser = this.<CustomUserEntity, QCustomUserEntity>createList("customUser", CustomUserEntity.class, QCustomUserEntity.class, PathInits.DIRECT2);

    public final ListPath<GroupAuthEntity, QGroupAuthEntity> groupAuthList = this.<GroupAuthEntity, QGroupAuthEntity>createList("groupAuthList", GroupAuthEntity.class, QGroupAuthEntity.class, PathInits.DIRECT2);

    public final StringPath groupName = createString("groupName");

    public final NumberPath<Long> groupSeq = createNumber("groupSeq", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath owner = createString("owner");

    public QGroupEntity(String variable) {
        super(GroupEntity.class, forVariable(variable));
    }

    public QGroupEntity(Path<? extends GroupEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGroupEntity(PathMetadata metadata) {
        super(GroupEntity.class, metadata);
    }

}

