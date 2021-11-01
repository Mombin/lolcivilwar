package kr.co.mcedu.group.model;

import kr.co.mcedu.group.entity.GroupEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Optional;

@Getter
@Setter
@ToString
public class GroupSaveRequest {
    private String groupName;

    public GroupEntity toEntity(){
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setOwner("admin");
        groupEntity.setGroupName(Optional.ofNullable(this.groupName).orElse(""));
        return groupEntity;
    }
}

