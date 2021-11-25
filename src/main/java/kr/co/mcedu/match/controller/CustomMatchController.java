package kr.co.mcedu.match.controller;

import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.match.model.request.CustomMatchSaveRequest;
import kr.co.mcedu.match.model.request.DiceRequest;
import kr.co.mcedu.match.model.response.DiceResponse;
import kr.co.mcedu.match.service.CustomMatchService;
import kr.co.mcedu.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/custom")
class CustomMatchController {
    private final CustomMatchService customMatchService;

    /**
     * 매치 결과 기록
     */
    @PutMapping("/match")
    public Object saveResult(@RequestBody CustomMatchSaveRequest customMatchSaveRequest) throws ServiceException {
        log.info("param: {}", customMatchSaveRequest);
        try {
            customMatchService.saveCustomMatchResult(customMatchSaveRequest);
        } catch (ServiceException exception) {
            return ResponseWrapper.fail(exception.getViewMessage()).build();
        }

        return new ResponseWrapper().build();
    }

    @PostMapping("/random-dice")
    public Object randomPosition(@RequestBody DiceRequest request) throws ServiceException {
        Map<String, DiceResponse> response = customMatchService.randomDice(request);
        return new ResponseWrapper().setData(response).build();
    }
}