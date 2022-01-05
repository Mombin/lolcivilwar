package kr.co.mcedu.riot.entity;

import kr.co.mcedu.riot.model.ChampionData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "champion_data")
@Table(name = "champion_data",schema = "lol")
public class ChampionDataEntity {

    @Id
    @Column(name = "champion_id")
    private Long championId;

    @Column(name = "champion_name")
    private String championName;

    @Column(name = "champion_korea_name")
    private String championKoreaName;

    public ChampionDataEntity(ChampionData championData) {
        this.championId = championData.getKey();
        this.championName = championData.getId();
        this.championKoreaName = championData.getName();
    }
}
