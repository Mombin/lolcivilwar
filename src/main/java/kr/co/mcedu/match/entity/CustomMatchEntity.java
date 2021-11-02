package kr.co.mcedu.match.entity;

import kr.co.mcedu.common.entity.BaseTimeEntity;
import kr.co.mcedu.group.entity.GroupEntity;
import lombok.Getter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity(name = "custom_match")
@Table(name = "custom_match", schema = "lol")
@SQLDelete(sql = "UPDATE lol.custom_match SET del_yn = true WHERE match_seq = ?")
@Where(clause = "del_yn = false")
@SequenceGenerator(sequenceName = "match_seq", initialValue = 1, allocationSize = 1, name = "match_seq_generator", schema = "lol")
public class CustomMatchEntity extends BaseTimeEntity {
    @Id
    @Column(name = "match_seq")
    @GeneratedValue(generator = "match_seq_generator", strategy = GenerationType.SEQUENCE)
    private Long matchSeq ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_seq", insertable = true, updatable = true, referencedColumnName = "group_seq")
    private GroupEntity group;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "customMatch", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MatchAttendeesEntity> matchAttendees = new ArrayList<>();

    @Column(name = "del_yn", columnDefinition = "boolean default false")
    private boolean delYn = false;

}
