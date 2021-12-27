package kr.co.mcedu.riot.data.controller;

import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.riot.data.service.GameDataService;
import kr.co.mcedu.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class ChampionDataController {
    private final GameDataService gameDataService;

    @GetMapping("/data")
    public Object insertData() throws ServiceException {
        gameDataService.insertChampionData();
        gameDataService.insertSpellData();
        return new ResponseWrapper().build();
    }
}
