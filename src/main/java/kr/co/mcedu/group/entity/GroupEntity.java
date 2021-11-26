package kr.co.mcedu.group.entity;

import kr.co.mcedu.common.entity.BaseTimeEntity;
import kr.co.mcedu.group.model.GroupResponse;
import kr.co.mcedu.match.entity.CustomMatchEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "group")
@Table(name = "group", schema = "lol")
@SequenceGenerator(sequenceName = "group_seq", initialValue = 1, allocationSize = 1, name = "group_seq_generator", schema = "lol")
public class GroupEntity extends BaseTimeEntity {
    @Id
    @Column(name = "group_seq")
    @GeneratedValue(generator = "group_seq_generator", strategy = GenerationType.SEQUENCE)
    private Long groupSeq ;

    @Column(name = "group_name", length = 30)
    private String groupName ;
    @Column(name = "owner", length = 30)
    private String owner ;

    @OneToMany(mappedBy = "group", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CustomUserEntity> customUser = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CustomMatchEntity>  customMatches = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GroupAuthEntity> groupAuthList = new ArrayList<>();

    public void addCustomUser(CustomUserEntity customUser){
        this.customUser.add(customUser);
        customUser.setGroup(this);
    }

    public void removeCustomUser(CustomUserEntity customUser){
        this.customUser.remove(customUser);
        customUser.setGroup(null);
    }

    public GroupEntity(GroupEntity groupEntity) {
        this.groupSeq = groupEntity.groupSeq;
        this.groupName = groupEntity.groupName;
        this.owner = groupEntity.owner;
        this.customUser = groupEntity.customUser;
        GroupAuthEntity auth = null;

    }
    public void addGroupAuth(GroupAuthEntity groupAuthEntity) {
        this.groupAuthList.add(groupAuthEntity);
        groupAuthEntity.setGroup(this);
    }
}


