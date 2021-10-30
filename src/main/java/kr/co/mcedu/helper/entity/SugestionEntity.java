package kr.co.mcedu.helper.entity;


import kr.co.mcedu.common.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity(name = "suggestion")
@Table(name = "suggestion", schema = "lol")
public class SugestionEntity extends BaseTimeEntity {
    @EmbeddedId
    private SuggestionId id ;

    @Column(name = "context",length = 300)
    private String context ;

}
