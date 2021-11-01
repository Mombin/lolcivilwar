package kr.co.mcedu.helper.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
@ToString
@EqualsAndHashCode
public class SuggestionId implements Serializable {
    @Column(name = "ip")
    private String ip;
    @Column(name = "date")
    private String date;

}
