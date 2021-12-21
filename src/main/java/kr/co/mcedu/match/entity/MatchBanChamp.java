package kr.co.mcedu.match.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "match_ban_champ")
@Getter
@Setter
@NoArgsConstructor
@Table(name = "match_ban_champ",schema = "lol")
@SequenceGenerator(sequenceName = "match_ban_seq", initialValue = 1, allocationSize = 1, name = "match_ban_seq_gen", schema = "lol")
public class MatchBanChamp {

    @Id
    @Column(name = "match_ban_seq")
    @GeneratedValue(generator = "match_ban_seq", strategy = GenerationType.SEQUENCE)
    private Long matchBanSeq;

    @OneToOne
    @JoinColumn(name = "match_seq" , referencedColumnName = "match_seq")
    private CustomMatchEntity customMatch;

    @Column(name = "ban_order")
    private String banOrder;

    @Column(name = "ban_champ_id")
    private Long banChampId;
}
