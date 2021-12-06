package kr.co.mcedu.user.service;

import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.user.entity.GroupInviteEntity;
import kr.co.mcedu.user.entity.UserAlarmEntity;
import kr.co.mcedu.user.entity.WebUserEntity;
import kr.co.mcedu.user.model.UserAlarmMessage;

import java.util.List;

public interface UserAlarmService {
    void sendNormalAlarm(WebUserEntity webUserEntity, String message);

    void sendInviteAlarm(WebUserEntity webUserEntity, GroupInviteEntity groupInviteEntity);

    void sendAlarm(UserAlarmEntity userAlarmEntity);

    Long getUnreadAlarmCount() throws AccessDeniedException;
    List<UserAlarmMessage> getUnreadMessage() throws AccessDeniedException;
    Object readMessage(Long alarmSeq) throws ServiceException;
}