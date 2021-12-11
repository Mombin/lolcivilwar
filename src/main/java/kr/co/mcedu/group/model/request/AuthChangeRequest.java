package kr.co.mcedu.group.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.mcedu.group.entity.GroupAuthEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(value = "AuthChangeRequest")
public class AuthChangeRequest {
    @ApiModelProperty(value = "유저 순번", example = "12", dataType = "Long")
    private Long userSeq;
    @ApiModelProperty(value = "그룹 권한", example = "USER", dataType = "GroupAuthEnum")
    private GroupAuthEnum changedAuth;
    @ApiModelProperty(value = "유저 아이디", example = "wunjo", dataType = "String")
    private String id;
}
