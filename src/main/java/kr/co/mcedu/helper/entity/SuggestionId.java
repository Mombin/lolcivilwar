package kr.co.mcedu.helper.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class SuggestionId implements Serializable {
    @Column(name = "ip")
    private String ip;
    @Column(name = "date")
    private String date;

    public SuggestionId(String ip, String date) {
        this.ip = ip;
        this.date = date;
    }
}
