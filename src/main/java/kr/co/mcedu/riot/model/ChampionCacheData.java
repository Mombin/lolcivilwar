package kr.co.mcedu.riot.model;

import kr.co.mcedu.riot.entity.ChampionDataEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChampionCacheData {

    private Long championId;
    private String championName;
    private String championKoreanName;

    public ChampionCacheData(final ChampionDataEntity entity) {
        this.championId = entity.getChampionId();
        this.championName = entity.getChampionName();
        this.championKoreanName = entity.getChampionKoreaName();
    }
}
