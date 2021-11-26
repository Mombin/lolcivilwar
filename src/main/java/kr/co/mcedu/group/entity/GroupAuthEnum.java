package kr.co.mcedu.group.entity;

import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.user.entity.WebUserEntity;
import kr.co.mcedu.utils.SessionUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public enum GroupAuthEnum {
    OWNER("소유자"),
    MANAGER("관리자"),
    MATCHER("주최자"),
    USER("사용자"),
    NONE("권한부족");

    GroupAuthEnum(String authName) {
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

    public static boolean isViewAbleAuth(final GroupAuthEnum groupAuth) {
        return groupAuth != NONE;
    }
}
