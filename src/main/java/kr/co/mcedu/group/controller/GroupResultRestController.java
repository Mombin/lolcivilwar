package kr.co.mcedu.group.controller;

import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.group.model.request.GroupResultRequest;
import kr.co.mcedu.group.service.GroupResultService;
import kr.co.mcedu.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group")
public class GroupResultRestController {

    private final GroupResultService groupResultService;

    @PostMapping("/v1/rank")
    public Object getRankDataBySeason(@RequestBody GroupResultRequest request) {
        log.info("request : {}", request.toString());

        try {
            return new ResponseWrapper().setData(groupResultService.getRankData(request)).build();
        } catch (ServiceException e) {
            return ResponseWrapper.fail(e.getViewMessage()).build();
        }
    }
}
