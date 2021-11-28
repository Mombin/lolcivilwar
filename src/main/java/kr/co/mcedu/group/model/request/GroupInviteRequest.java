package kr.co.mcedu.group.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "GroupInviteRequest")
public class GroupInviteRequest {
    @ApiModelProperty(value = "초대할 그룹", example = "17", dataType = "Long")
    private Long groupSeq;
    @ApiModelProperty(value = "초대할 유저", example = "20", dataType = "Long")
    private Long userSeq;
    @ApiModelProperty(value = "초대할 유저 LOLCW TAG", example = "AB371", dataType = "String", notes = "이중 체크를 위한 태그첨부")
    private String lolcwTag;
}
