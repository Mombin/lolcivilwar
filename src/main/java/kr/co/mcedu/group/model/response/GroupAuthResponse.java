package kr.co.mcedu.group.model.response;

import kr.co.mcedu.group.entity.GroupAuthEntity;
import kr.co.mcedu.group.entity.GroupAuthEnum;
import kr.co.mcedu.group.entity.GroupEntity;
import kr.co.mcedu.user.entity.WebUserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class GroupAuthResponse {
    private GroupAuthEnum groupAuth;
    private String userId;
    private Long groupSeq;
    private Long userSeq;

    public GroupAuthResponse(GroupAuthEntity entity) {
        this.groupAuth = entity.getGroupAuth();
        Optional<WebUserEntity> webUser = Optional.ofNullable(entity.getWebUser());
        this.userId = webUser.map(WebUserEntity::getUserId).orElse(null);
        this.userSeq = webUser.map(WebUserEntity::getUserSeq).orElse(null);
        this.groupSeq = Optional.ofNullable(entity.getGroupEntity()).map(GroupEntity::getGroupSeq).orElse(null);
    }

    public static List<GroupAuthResponse> of(List<GroupAuthEntity> groupAuthList ) {
        List<GroupAuthResponse> resultList = new ArrayList<>();
        groupAuthList.forEach(it -> resultList.add(new GroupAuthResponse(it)));
        return resultList;
    }
}
