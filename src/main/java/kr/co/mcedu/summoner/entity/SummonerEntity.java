package kr.co.mcedu.summoner.entity;

import kr.co.mcedu.common.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity(name = "summoner")
@Table(name = "summoner", schema = "lol")
public class SummonerEntity extends BaseTimeEntity {
    @Id
    @Column(name = "account_id", length = 60)
    private String accountId ;

    @Column
    private String id;

    @Column (name = "puuid", length = 80)
    private String puuid ;

    @Column(name = "name", length = 20)
    private String name ;

    @Column(name = "search_name", length = 20)
    private String searchName;

    @Column(name = "profile_icon_id")
    private int profileIconId ;

    @Column(name = "revision_date")
    private Long revisionDate ;

    @Column(name = "summoner_level")
    private int summonerLevel;

}
