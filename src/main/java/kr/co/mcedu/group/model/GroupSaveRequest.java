package kr.co.mcedu.group.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupSaveRequest {
    private String groupName;




//    fun toEntity(): GroupEntity {
//        return GroupEntity().also {
//            it.owner = "admin"
//            it.groupName = this.groupName ?: ""
//        }
//    }
//
//    override fun toString(): String {
//        return "GroupSaveRequest(groupName=$groupName)"
//    }

    //TODO : 하기부분을 상기부분처럼 구현할 예정..
//    public GroupEntity toEntity(){
//        GroupEntity var1 = new GroupEntity();
//        boolean var2 = false;
//        boolean var3 = false;
//        var1.setOwner("admin");
//        String groupName = this.groupName;
//        if (groupName == null) {
//            groupName = "";
//        }
//
//        var1.setGroupName(groupName);
//        return var1;
//    }
//}
}
