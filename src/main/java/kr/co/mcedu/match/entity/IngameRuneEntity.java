package kr.co.mcedu.match.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "ingame_rune")
@Table(name = "ingame_rune", schema = "lol")
@SequenceGenerator(sequenceName = "ingame_rune_seq", initialValue = 1, allocationSize = 1, name = "ingame_rune_seq_gen", schema = "lol")
public class IngameRuneEntity {

    @Id
    @Column(name = "rune_seq")
    @GeneratedValue(generator = "ingame_rune_seq", strategy = GenerationType.SEQUENCE)
    private Long runeSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendees_seq",referencedColumnName ="attendees_seq")
    private MatchAttendeesEntity attendeesSeq;

    @Column(name = "rune_type")
    private String runeType;

    @Column(name = "rune_id")
    private Long rundId;
}
