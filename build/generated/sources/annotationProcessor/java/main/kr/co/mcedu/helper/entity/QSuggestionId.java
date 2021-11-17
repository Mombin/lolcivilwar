package kr.co.mcedu.helper.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSuggestionId is a Querydsl query type for SuggestionId
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QSuggestionId extends BeanPath<SuggestionId> {

    private static final long serialVersionUID = 260527315L;

    public static final QSuggestionId suggestionId = new QSuggestionId("suggestionId");

    public final StringPath date = createString("date");

    public final StringPath ip = createString("ip");

    public QSuggestionId(String variable) {
        super(SuggestionId.class, forVariable(variable));
    }

    public QSuggestionId(Path<? extends SuggestionId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSuggestionId(PathMetadata metadata) {
        super(SuggestionId.class, metadata);
    }

}

