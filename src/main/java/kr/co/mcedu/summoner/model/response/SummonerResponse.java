package kr.co.mcedu.summoner.model.response;

import kr.co.mcedu.summoner.entity.SummonerEntity;
import kr.co.mcedu.utils.ModelUtils;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SummonerResponse {
    private String name = "";
    private Integer summonerLevel = 0;
    private LocalDateTime modifiedDate;
    private Integer profileIconId = 0;
    private String accountId = "";
    public static SummonerResponse convert(SummonerEntity summonerEntity) {
        return ModelUtils.map(summonerEntity, SummonerResponse.class);
    }
}