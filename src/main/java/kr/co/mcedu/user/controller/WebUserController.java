package kr.co.mcedu.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.user.model.UserInfo;
import kr.co.mcedu.user.service.UserAlarmService;
import kr.co.mcedu.user.service.WebUserService;
import kr.co.mcedu.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * web user 에 관한 RestController
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Api(value = "WebUserController")
class WebUserController {
    private static final String API_TAG = "web-user";

    private final UserAlarmService userAlarmService;
    private final WebUserService webUserService;

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
    @ApiOperation(value = "unreadMessage", tags = API_TAG, notes = "안읽은 메시지 가져오기")
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

    @ApiOperation(value = "userByLolTag", tags = API_TAG, notes = "lolcw tag로 사람찾기")
    @GetMapping("/by-tag/{lolcwTag}")
    public Object getUserByLolTag(@PathVariable String lolcwTag) throws ServiceException {
        UserInfo userInfo = webUserService.getUserInfoByLolcwTag(lolcwTag);
        return new ResponseWrapper().setData(userInfo).build();
    }
}