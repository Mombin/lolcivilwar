package kr.co.mcedu.group.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCustomUserEntity is a Querydsl query type for CustomUserEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCustomUserEntity extends EntityPathBase<CustomUserEntity> {

    private static final long serialVersionUID = -598143726L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCustomUserEntity customUserEntity = new QCustomUserEntity("customUserEntity");

    public final BooleanPath delYn = createBoolean("delYn");

    public final QGroupEntity group;

    public final StringPath nickname = createString("nickname");

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final kr.co.mcedu.summoner.entity.QSummonerEntity summonerEntity;

    public final StringPath summonerName = createString("summonerName");

    public QCustomUserEntity(String variable) {
        this(CustomUserEntity.class, forVariable(variable), INITS);
    }

    public QCustomUserEntity(Path<? extends CustomUserEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCustomUserEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCustomUserEntity(PathMetadata metadata, PathInits inits) {
        this(CustomUserEntity.class, metadata, inits);
    }

    public QCustomUserEntity(Class<? extends CustomUserEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.group = inits.isInitialized("group") ? new QGroupEntity(forProperty("group")) : null;
        this.summonerEntity = inits.isInitialized("summonerEntity") ? new kr.co.mcedu.summoner.entity.QSummonerEntity(forProperty("summonerEntity")) : null;
    }

}

