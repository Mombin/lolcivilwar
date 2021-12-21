package kr.co.mcedu.match.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "match_pick_chcamp")
@Getter
@Setter
@Table(name = "match_pick_champ", schema = "lol")
@NoArgsConstructor
public class MatchPickChampionEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendees_seq",referencedColumnName ="attendees_seq")
    private MatchAttendeesEntity attendeesSeq;

    @Column(name = "spell1_id")
    private Long spell1Id;

    @Column(name = "spell2_id")
    private Long spell2Id;

    @Column(name = "pick_champ_id")
    private Long pickChampId;

    @Column(name = "main_rune")
    private Long mainRune;

    @Column(name = "sub_rune")
    private Long subRune;

}
