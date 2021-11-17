package kr.co.mcedu.helper.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSuggestionEntity is a Querydsl query type for SuggestionEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSuggestionEntity extends EntityPathBase<SuggestionEntity> {

    private static final long serialVersionUID = -1723166821L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSuggestionEntity suggestionEntity = new QSuggestionEntity("suggestionEntity");

    public final kr.co.mcedu.common.entity.QBaseTimeEntity _super = new kr.co.mcedu.common.entity.QBaseTimeEntity(this);

    public final StringPath context = createString("context");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QSuggestionId id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public QSuggestionEntity(String variable) {
        this(SuggestionEntity.class, forVariable(variable), INITS);
    }

    public QSuggestionEntity(Path<? extends SuggestionEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSuggestionEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSuggestionEntity(PathMetadata metadata, PathInits inits) {
        this(SuggestionEntity.class, metadata, inits);
    }

    public QSuggestionEntity(Class<? extends SuggestionEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QSuggestionId(forProperty("id")) : null;
    }

}

