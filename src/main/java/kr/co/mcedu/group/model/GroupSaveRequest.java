package kr.co.mcedu.group.model;

import kr.co.mcedu.group.entity.GroupEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupSaveRequest {
    private String groupName ;

    public GroupEntity toEntity(){
        GroupEntity var1 = new GroupEntity();
        boolean var2 = false;
        boolean var3 = false;
        var1.setOwner("admin");
        String groupName = this.groupName;
        if (groupName == null) {
            groupName = "";
        }

        var1.setGroupName(groupName);
        return var1;
    }
}
