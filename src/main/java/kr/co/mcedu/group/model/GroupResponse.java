package kr.co.mcedu.group.model;

import kr.co.mcedu.group.entity.GroupAuthEntity;
import kr.co.mcedu.group.entity.GroupEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupResponse {

    private long groupSeq;

    private String groupName;

    private String owner;

    private List customUser;

    private GroupAuthEntity auth;

    public GroupResponse(GroupEntity groupEntity) {

    }
}
