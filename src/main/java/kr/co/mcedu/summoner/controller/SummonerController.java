package kr.co.mcedu.summoner.controller;

import kr.co.mcedu.summoner.model.response.SummonerResponse;
import kr.co.mcedu.summoner.service.SummonerService;
import kr.co.mcedu.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/summoner")
public class SummonerController {
    private final SummonerService summonerService;

    @GetMapping("/search")
    public Object searchSummonerById(@RequestParam String summonerName) {
        SummonerResponse response = summonerService.getSummonerInCache(summonerName);
        ResponseWrapper result;
        if (response == null) {
            result = ResponseWrapper.fail();
        } else {
            result = new ResponseWrapper().setData(response);
        }
        return result.build();
    }
}