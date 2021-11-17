package kr.co.mcedu.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QWebUserEntity is a Querydsl query type for WebUserEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWebUserEntity extends EntityPathBase<WebUserEntity> {

    private static final long serialVersionUID = 1148217265L;

    public static final QWebUserEntity webUserEntity = new QWebUserEntity("webUserEntity");

    public final kr.co.mcedu.common.entity.QBaseTimeEntity _super = new kr.co.mcedu.common.entity.QBaseTimeEntity(this);

    public final StringPath authority = createString("authority");

    public final BooleanPath confirm = createBoolean("confirm");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath password = createString("password");

    public final StringPath refreshToken = createString("refreshToken");

    public final StringPath userId = createString("userId");

    public final NumberPath<Long> userSeq = createNumber("userSeq", Long.class);

    public QWebUserEntity(String variable) {
        super(WebUserEntity.class, forVariable(variable));
    }

    public QWebUserEntity(Path<? extends WebUserEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWebUserEntity(PathMetadata metadata) {
        super(WebUserEntity.class, metadata);
    }

}

