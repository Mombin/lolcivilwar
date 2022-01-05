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
}
