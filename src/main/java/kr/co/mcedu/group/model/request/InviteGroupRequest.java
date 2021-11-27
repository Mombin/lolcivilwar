package kr.co.mcedu.group.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InviteGroupRequest {
    private String lolcwTag;
    private Long groupSeq;
    private Long invitedUserSeq;
}
