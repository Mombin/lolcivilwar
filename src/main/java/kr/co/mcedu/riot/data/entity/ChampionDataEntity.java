package kr.co.mcedu.riot.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity(name = "champion_data")
@Table(name = "champion_data",schema = "lol")
public class ChampionDataEntity {

    @Id
    @Column(name = "champion_id")
    private Long championId;

    @Column(name = "champion_name")
    private String championName;
}
