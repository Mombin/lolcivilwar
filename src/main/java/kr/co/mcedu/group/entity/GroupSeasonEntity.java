package kr.co.mcedu.group.entity;

import kr.co.mcedu.common.entity.BaseTimeEntity;
import kr.co.mcedu.group.model.response.GroupSeasonResponse;
import kr.co.mcedu.user.entity.WebUserEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "group_season")
@Table(name = "group_season", schema = "lol")
@SequenceGenerator(sequenceName = "group_season_seq", initialValue = 1, allocationSize = 1, name = "group_season_seq_gen", schema = "lol")
public class GroupSeasonEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "group_season_seq_gen", strategy = GenerationType.SEQUENCE)
    @Column(name = "group_season_seq")
    private Long groupSeasonSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_seq", referencedColumnName = "group_seq", insertable = true, updatable = true)
    private GroupEntity group = null;

    private String seasonName;

    @ManyToOne
    @JoinColumn(name = "created_user_seq", referencedColumnName = "user_seq")
    private WebUserEntity createUser;
    @ManyToOne
    @JoinColumn(name = "modified_user_seq", referencedColumnName = "user_seq")
    private WebUserEntity modifiedUser;

    private Boolean defaultSeason;

    public static GroupSeasonEntity defaultSeason(final WebUserEntity owner, final GroupEntity group) {
        GroupSeasonEntity groupSeasonEntity = new GroupSeasonEntity();
        groupSeasonEntity.setSeasonName("기본시즌");
        groupSeasonEntity.setDefaultSeason(true);
        groupSeasonEntity.setCreateUser(owner);
        groupSeasonEntity.setGroup(group);
        return groupSeasonEntity;
    }

    public GroupSeasonResponse toResponse() {
        GroupSeasonResponse groupSeasonResponse = new GroupSeasonResponse();
        groupSeasonResponse.setSeasonSeq(this.groupSeasonSeq);
        groupSeasonResponse.setSeasonName(this.seasonName);
        return groupSeasonResponse;
    }
}
