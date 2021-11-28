package kr.co.mcedu.user.model;

import lombok.Getter;

/**
 * Alarm Type
 */
@Getter
public enum UserAlarmType {
    MESSAGE(""), INVITE("[%s] 에서 당신을 초대했습니다. 가입하시겠습니까?"), LINK("");

    private final String message;

    UserAlarmType(String message) {
        this.message = message;
    }
}