package kr.co.mcedu.summoner.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSummonerEntity is a Querydsl query type for SummonerEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSummonerEntity extends EntityPathBase<SummonerEntity> {

    private static final long serialVersionUID = -322340923L;

    public static final QSummonerEntity summonerEntity = new QSummonerEntity("summonerEntity");

    public final kr.co.mcedu.common.entity.QBaseTimeEntity _super = new kr.co.mcedu.common.entity.QBaseTimeEntity(this);

    public final StringPath accountId = createString("accountId");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath id = createString("id");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> profileIconId = createNumber("profileIconId", Integer.class);

    public final StringPath puuid = createString("puuid");

    public final NumberPath<Long> revisionDate = createNumber("revisionDate", Long.class);

    public final StringPath searchName = createString("searchName");

    public final NumberPath<Integer> summonerLevel = createNumber("summonerLevel", Integer.class);

    public QSummonerEntity(String variable) {
        super(SummonerEntity.class, forVariable(variable));
    }

    public QSummonerEntity(Path<? extends SummonerEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSummonerEntity(PathMetadata metadata) {
        super(SummonerEntity.class, metadata);
    }

}

