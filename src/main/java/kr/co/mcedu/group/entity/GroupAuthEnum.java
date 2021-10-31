package kr.co.mcedu.group.entity;

public enum GroupAuthEnum {
    OWNER("소유자"),
    MANAGER("관리자"),
    MATCHER("주최자"),
    USER("사용자"),
    NONE("권한부족");

    GroupAuthEnum(String authName) {
    }


    // bolle
    public boolean isMatchableAuth(GroupAuthEnum groupAuth){
        if((groupAuth == OWNER || groupAuth == MANAGER || groupAuth == MATCHER)) {
            return  true;
        }else{
            return false;
        }
    }

    public  boolean isManageableAuth(GroupAuthEnum groupAuth) {
        if ((groupAuth == OWNER || groupAuth == MANAGER)) {
            return true;
        } else {
            return false;
        }
    }
// TODO: 하기 부분 이해안됬음 ㅠ..
//companion object {
//        /**
//         * 내전 생성 가능 권한 체크
//         * @param groupAuth 체크할 권한
//         * @return 매치가능 권한일시 true
//         */
//        fun isMatchableAuth(groupAuth: GroupAuth) = (groupAuth == OWNER || groupAuth == MANAGER || groupAuth == MATCHER)
//
//        /**
//         * 그룹 관리 가능 권한 체크
//         * @param groupAuth 체크할 권한
//         * @return 관리가능 권한일시 true
//         */
//        fun isManageableAuth(groupAuth: GroupAuth?) = (groupAuth == OWNER || groupAuth == MANAGER)
//
//
//        /**
//         * 그룹 권한 체크
//         * @param authChecker 체크할 함수
//         * @param groupAuthList 권한리스트
//         */
//        @Throws(ServiceException::class)
//        fun authorityCheck(authChecker: (GroupAuth) -> Boolean, groupAuthList: List<GroupAuthEntity>) {
//            val userId = SessionUtils.getId()
//
//            if (!groupAuthList.stream().filter {
//                it.webUser?.userId == userId
//            }.anyMatch {
//                authChecker(it.groupAuth ?: NONE)
//            }) {
//                throw AccessDeniedException("권한이 부족합니다.")
//            }
//        }
//    }
}
