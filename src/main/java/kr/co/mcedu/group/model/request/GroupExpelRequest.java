package kr.co.mcedu.group.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(value = "GroupExpelRequest")
public class GroupExpelRequest {
    @ApiModelProperty(value = "추방할 그룹", example = "17", dataType = "Long")
    private Long groupSeq;
    @ApiModelProperty(value = "추방할 유저", example = "20", dataType = "Long")
    private Long userSeq;
}
