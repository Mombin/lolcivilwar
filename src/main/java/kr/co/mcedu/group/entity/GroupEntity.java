package kr.co.mcedu.group.entity;

import kr.co.mcedu.common.entity.BaseTimeEntity;
import kr.co.mcedu.group.model.GroupResponse;
import kr.co.mcedu.match.entity.CustomMatchEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "group")
@Table(name = "group", schema = "lol")
@SequenceGenerator(sequenceName = "group_seq", initialValue = 1, allocationSize = 1, name = "group_seq_generator", schema = "lol")
public class GroupEntity extends BaseTimeEntity {
    @Id
    @Column(name = "group_seq")
    @GeneratedValue(generator = "group_seq_generator", strategy = GenerationType.SEQUENCE)
    private long groupSeq ;

    @Column(name = "group_name", length = 30)
    private String groupName ;
    @Column(name = "owner", length = 30)
    private String owner ;

    @OneToMany(mappedBy = "group", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    private ArrayList<CustomUserEntity> customUser ;

    @OneToMany(mappedBy = "group", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
    private ArrayList<CustomMatchEntity>  customMatches ;

    @OneToMany(mappedBy = "group", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
    private ArrayList<GroupAuthEntity> groupAuthList ;

    private void addCustomUser(CustomUserEntity customUser){
        this.customUser.add(customUser);
        customUser.setGroupEntity(this);
    }

    private void removeCustomUser(CustomUserEntity customUser){
        this.customUser.remove(customUser);
        customUser.setGroupEntity(null);
    }

    public final GroupResponse toGroupResponse() {
        return new GroupResponse(this);
    }

    public GroupEntity(GroupEntity groupEntity) {
        this.groupSeq = groupEntity.groupSeq;
        this.groupName = groupEntity.groupName;
        this.owner = groupEntity.owner;
        this.customUser = groupEntity.customUser;
        GroupAuthEntity auth = null;

    }
}


