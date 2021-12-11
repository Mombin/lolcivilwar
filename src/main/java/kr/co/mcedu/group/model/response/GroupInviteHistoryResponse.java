package kr.co.mcedu.group.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.mcedu.user.entity.GroupInviteEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "GroupInviteHistoryResponse")
public class GroupInviteHistoryResponse {
    @ApiModelProperty(value = "초대 순번", example = "17", dataType = "Long")
    private Long inviteSeq;
    @ApiModelProperty(value = "초대 요청한 유저 아이디", example = "wunjo", dataType = "String")
    private String requestUserId;
    @ApiModelProperty(value = "초대된 유저 아이디", example = "buj6842", dataType = "String")
    private String invitedUserId;
    @ApiModelProperty(value = "초대결과", example = "N", dataType = "String")
    private String inviteResult;
    @ApiModelProperty(value = "초대유효결과", example = "Y", dataType = "String")
    private String expireResult;
    @ApiModelProperty(value = "초대한 시간", example = "Y", dataType = "String")
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime invitedDate;
    @ApiModelProperty(value = "마지막 응답 시간", example = "Y", dataType = "String")
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedDate;

    public GroupInviteHistoryResponse(GroupInviteEntity entity) {
        this.requestUserId = entity.getUser().getUserId();
        this.inviteSeq = entity.getGroupInviteSeq();
        this.invitedDate = entity.getInvitedDate();
        Boolean entityInviteResult = entity.getInviteResult();
        if (entityInviteResult == null) {
            this.inviteResult = "";
        } else {
            this.inviteResult = entityInviteResult ? "Y" : "N";
        }
        this.invitedUserId = entity.getInvitedUser().getUserId();
        this.modifiedDate = entity.getModifiedDate();
        this.expireResult = Boolean.TRUE.equals(entity.getExpireResult()) ? "Y" : "N";
    }
}
