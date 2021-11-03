package kr.co.mcedu.match.entity;


import kr.co.mcedu.common.entity.BaseTimeEntity;
import kr.co.mcedu.group.entity.CustomUserEntity;
import kr.co.mcedu.match.model.CustomMatchResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "match_attendees")
@Table(name = "match_attendees", schema = "lol")
@SQLDelete(sql = "UPDATE lol.match_attendees SET del_yn = true WHERE attendees_seq = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "del_yn = false")
@NoArgsConstructor
@SequenceGenerator(sequenceName = "attendees_seq", initialValue = 1, allocationSize = 1, name = "attendees_seq_generator", schema = "lol")
public class MatchAttendeesEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "attendees_seq_generator", strategy = GenerationType.SEQUENCE)
    private Long attendeesSeq ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_seq", insertable = true, updatable = true, referencedColumnName = "match_seq")
    private CustomMatchEntity customMatch;

    @Column(name = "position")
    private String position ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "custom_user_seq", insertable = true, updatable = true, referencedColumnName = "seq")
    private CustomUserEntity customUserEntity;

    @Column(name = "team", length = 1)
    private  String team;
    @Column(name = "match_result")
    private boolean matchResult = false;

    @Column(name = "del_yn", columnDefinition = "boolean default false")
    private boolean delYn  = false;

    public MatchAttendeesEntity(CustomMatchResult customMatchResult) {
        this.matchResult = customMatchResult.getResult();
        this.position = customMatchResult.getPosition();
        this.team = customMatchResult.getTeam();
        this.customUserEntity = customMatchResult.getCustomUser();
        this.customMatch = customMatchResult.getCustomMatch();
    }

    @PreRemove
    public void removeAttendess(){
        delYn = true;
    }

}
