package kr.co.mcedu.user.controller;

import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.user.service.UserAlarmService;
import kr.co.mcedu.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * web user 에 관한 RestController
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
class WebUserController {
    private final UserAlarmService userAlarmService;

    /**
     * alarm count
     */
    @GetMapping("/alarm/count")
    public Object getUnreadAlarmCount() throws AccessDeniedException {
        return new ResponseWrapper().setData(userAlarmService.getUnreadAlarmCount()).build();
    }

    /**
     * 알림 리스트
     */
    @GetMapping("/alarm")
    public Object getUnreadMessage() throws AccessDeniedException {
        return new ResponseWrapper().setData(userAlarmService.getUnreadMessage()).build();
    }

    /**
     * 알림 읽기
     */
    @PostMapping("/alarm/read/{alarmSeq}")
    public Object readMessage(@PathVariable Long alarmSeq) throws ServiceException {
        return new ResponseWrapper().setData(userAlarmService.readMessage(alarmSeq)).build();
    }
}