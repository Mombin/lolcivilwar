package kr.co.mcedu.group.entity;

import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.user.entity.WebUserEntity;
import kr.co.mcedu.utils.SessionUtils;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Getter
public enum GroupAuthEnum {
    OWNER("소유자", 4),
    MANAGER("관리자", 3),
    MATCHER("주최자", 2),
    USER("사용자", 1),
    NONE("권한부족", 0);

    private final String authName;
    private final int order;

    GroupAuthEnum(String authName, int order) {
        this.authName = authName;
        this.order = order;
    }


    // bolle
    public static boolean isMatchableAuth(GroupAuthEnum groupAuth){
        return groupAuth == OWNER || groupAuth == MANAGER || groupAuth == MATCHER;
    }

    public  static boolean isManageableAuth(GroupAuthEnum groupAuth) {
        return groupAuth == OWNER || groupAuth == MANAGER;
    }

    public static void authorityCheck(Function<GroupAuthEnum, Boolean> authChecker, List<GroupAuthEntity> groupAuthList) throws ServiceException {
        String userId = SessionUtils.getId();

        boolean authCheck = groupAuthList.stream().filter(
                        groupAuthEntity -> Optional.ofNullable(groupAuthEntity.getWebUser()).map(WebUserEntity::getUserId).orElse("").equals(userId))
                .noneMatch(groupAuthEntity -> authChecker.apply(Optional.ofNullable(groupAuthEntity.getGroupAuth()).orElse(NONE)));
        if(authCheck){
            throw new AccessDeniedException("권한이 부족합니다.");
        }

    }
}
