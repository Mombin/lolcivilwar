package kr.co.mcedu.group.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.group.model.request.GroupExpelRequest;
import kr.co.mcedu.group.model.request.GroupInviteRequest;
import kr.co.mcedu.group.model.request.ReplyInviteRequest;
import kr.co.mcedu.group.service.GroupUserService;
import kr.co.mcedu.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Api(value = "GroupUserController")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group")
public class GroupUserController {

    private static final String API_TAG = "group-user";
    private final GroupUserService groupUserService;

    @ApiOperation(value = "expelUser", tags = API_TAG, notes = "그룹에서 추방하기")
    @DeleteMapping("/v1/expel-user")
    public Object expelUser(@RequestBody GroupExpelRequest request) throws ServiceException {
        groupUserService.expelUser(request);
        return new ResponseWrapper().build();
    }

    @ApiOperation(value = "inviteUser", tags = API_TAG, notes = "그룹에 초대하기")
    @PostMapping("/v1/invite-user")
    public Object inviteUser(@RequestBody GroupInviteRequest request) throws ServiceException {
        groupUserService.inviteUser(request);
        return new ResponseWrapper().build();
    }

    @ApiOperation(value = "replyInviteMessage", tags = API_TAG, notes = "그룹 초대에 응답하기")
    @PostMapping("/v1/invite-result")
    public Object replyInviteMessage(@RequestBody ReplyInviteRequest request) throws ServiceException {
        groupUserService.replyInviteMessage(request);
        return new ResponseWrapper().build();
    }
}
