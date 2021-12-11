package kr.co.mcedu.group.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(value = "GroupAuthChangeRequest")
public class GroupAuthChangeRequest {
    @ApiModelProperty(value = "그룹 순번", example = "17", dataType = "Long")
    private Long groupSeq;
    @ApiModelProperty(value = "그룹 권한 변경 리스트", dataType = "AuthChangeRequest")
    private List<AuthChangeRequest> targets = new ArrayList<>();
}
