package kr.co.mcedu.group.controller;

import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.group.model.request.CustomUserSynergyRequest;
import kr.co.mcedu.group.model.request.GroupResultRequest;
import kr.co.mcedu.group.model.response.CustomUserSynergyResponse;
import kr.co.mcedu.group.service.GroupResultService;
import kr.co.mcedu.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group")
public class GroupResultRestController {

    private final GroupResultService groupResultService;

    @PostMapping("/v1/rank")
    public Object getRankDataBySeason(@RequestBody GroupResultRequest request) {
        this.requestLog(request);
        try {
            return new ResponseWrapper().setData(groupResultService.getRankData(request)).build();
        } catch (ServiceException e) {
            return ResponseWrapper.fail(e.getViewMessage()).build();
        }
    }

    @PostMapping("/v1/match_attendees")
    public Object getMatchAttendees(@RequestBody GroupResultRequest request) throws ServiceException {
        this.requestLog(request);
        return new ResponseWrapper().setData(groupResultService.getMatchAttendees(request)).build();
    }

    @GetMapping("/v1/synergy")
    public Object getSynergy(@ModelAttribute GroupResultRequest request) throws ServiceException {
        this.requestLog(request);
        CustomUserSynergyResponse data = groupResultService.calculateSynergy(request);
        return new ResponseWrapper().setData(data).build();
    }

    private void requestLog(Object param) {
        log.info("request: {}", param.toString());
    }
}
