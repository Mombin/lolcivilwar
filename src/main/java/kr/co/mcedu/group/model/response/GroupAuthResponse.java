package kr.co.mcedu.group.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.mcedu.group.entity.GroupAuthEntity;
import kr.co.mcedu.group.entity.GroupAuthEnum;
import kr.co.mcedu.group.entity.GroupEntity;
import kr.co.mcedu.user.entity.WebUserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@ApiModel(value = "GroupAuthResponse")
@NoArgsConstructor
public class GroupAuthResponse {
    @ApiModelProperty(value = "그룹 권한", example = "MATCHER", dataType = "String")
    private GroupAuthEnum groupAuth;
    @ApiModelProperty(value = "유저 아이디", example = "wunjo", dataType = "String")
    private String userId;
    @ApiModelProperty(value = "그룹 순번", example = "17", dataType = "Long")
    private Long groupSeq;
    @ApiModelProperty(value = "유저 순번", example = "16", dataType = "Long")
    private Long userSeq;

    public GroupAuthResponse(GroupAuthEntity entity) {
        this.groupAuth = entity.getGroupAuth();
        Optional<WebUserEntity> webUser = Optional.ofNullable(entity.getWebUser());
        this.userId = webUser.map(WebUserEntity::getUserId).orElse(null);
        this.userSeq = webUser.map(WebUserEntity::getUserSeq).orElse(null);
        this.groupSeq = Optional.ofNullable(entity.getGroup()).map(GroupEntity::getGroupSeq).orElse(null);
    }
}
