package kr.co.mcedu.group.entity;

public enum GroupAuthEnum {
    OWNER("소유자"),
    MANAGER("관리자"),
    MATCHER("주최자"),
    USER("사용자"),
    NONE("권한부족");

    GroupAuthEnum(String authName) {
    }

    public boolean isMatchableAuth(GroupAuthEnum groupAuth){
        if((groupAuth == OWNER || groupAuth == MANAGER || groupAuth == MATCHER)){
            return  true;
        }else{
            return false;
        }
    }

    public  boolean isManageableAuth(GroupAuthEnum groupAuth){
        if((groupAuth == OWNER || groupAuth == MANAGER)){
            return true;
        }else{
            return false;
        }
    }
//보류..
//    private void authorityCheck(GroupAuthEnum authChecker , List<GroupAuthEntity> groupAuthEntityList){
//    }
}
