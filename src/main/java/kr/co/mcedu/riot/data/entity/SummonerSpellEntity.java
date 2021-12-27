package kr.co.mcedu.riot.data.entity;

import kr.co.mcedu.riot.data.model.SummonerSpellData;
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
@Entity(name = "summoner_spell_data")
@Table(name = "summoner_spell_data", schema = "lol")
public class SummonerSpellEntity {

    @Id
    @Column(name = "summoner_spell_id")
    private Long summonerSpellId;

    @Column(name = "summoner_spell_name")
    private String summonerSpellName;

    public SummonerSpellEntity (SummonerSpellData summonerSpellData) {
        this.summonerSpellId = summonerSpellData.getKey();
        this.summonerSpellName = summonerSpellData.getId();
    }
}
