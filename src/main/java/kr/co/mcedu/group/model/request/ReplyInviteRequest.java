package kr.co.mcedu.group.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "ReplyInviteRequest")
public class ReplyInviteRequest {
    @ApiModelProperty(value = "응답할 알림", example = "17", dataType = "Long")
    private Long alarmSeq;
    @ApiModelProperty(value = "응답 Y/N", example = "Y", dataType = "String")
    private String result;
    @ApiModelProperty(value = "초대 순번", example = "12", dataType = "Long")
    private Long inviteSeq;

    public boolean isValidRequest() {
        return "Y".equals(result) || "N".equals(result) || alarmSeq != null || inviteSeq != null;
    }
}
