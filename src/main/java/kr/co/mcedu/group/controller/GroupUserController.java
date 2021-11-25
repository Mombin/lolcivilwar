package kr.co.mcedu.group.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.group.model.request.GroupExpelRequest;
import kr.co.mcedu.group.service.GroupUserService;
import kr.co.mcedu.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Object expelUser(@RequestBody GroupExpelRequest request) {
        log.info("request : {}", request.toString());
        try {
            groupUserService.expelUser(request);
        } catch (ServiceException exception) {
            log.info(exception.getViewMessage());
            return ResponseWrapper.fail(exception.getViewMessage()).build();
        }

        return new ResponseWrapper().build();
    }
}
