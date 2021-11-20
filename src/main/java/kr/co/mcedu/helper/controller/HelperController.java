package kr.co.mcedu.helper.controller;

import kr.co.mcedu.config.exception.AlreadyDataExistException;
import kr.co.mcedu.helper.model.SuggestionRequest;
import kr.co.mcedu.helper.service.HelperService;
import kr.co.mcedu.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/helper")
public class HelperController {
    private final HelperService helperService;

    @PutMapping("save")
    public Object saveSuggestion(@RequestBody SuggestionRequest suggestionRequest) {
        ResponseWrapper result;
        try {
            if (helperService.saveSuggestion(suggestionRequest)) {
                result = new ResponseWrapper();
            } else {
                result = ResponseWrapper.fail();
            }
        } catch (AlreadyDataExistException ex) {
            result = ResponseWrapper.fail("이미 오늘은 등록하셨습니다. 내일 새롭게 건의해주세요.");
        }
        return result.build();
    }
}