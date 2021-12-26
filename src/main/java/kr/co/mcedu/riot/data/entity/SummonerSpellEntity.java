package kr.co.mcedu.riot.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity(name = "summoner_spell_data")
@Table(name = "summoner_spell_data", schema = "lol")
public class SummonerSpellEntity {

    @Id
    @Column(name = "summoner_spell_id")
    private Long summonerSpellId;

    @Column(name = "summoner_spell_name")
    private String summonerSpellName;
}
