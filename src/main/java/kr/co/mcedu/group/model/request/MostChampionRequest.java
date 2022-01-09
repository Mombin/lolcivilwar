package kr.co.mcedu.group.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MostChampionRequest {
    private String position;
    private Long customUserSeq;
    private Long groupSeasonSeq;

    public String getCacheKey () {
        return String.format("%s_%s_%s", this.groupSeasonSeq, this.customUserSeq, this.position);
    }
}
