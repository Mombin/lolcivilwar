package kr.co.mcedu.riot.controller;

import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.riot.service.RiotDataService;
import kr.co.mcedu.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class ChampionDataController {
    private final RiotDataService riotDataService;

    @GetMapping("/data")
    public Object insertData() throws ServiceException {
        riotDataService.insertChampionData();
        riotDataService.insertSpellData();
        return new ResponseWrapper().build();
    }

    @PostMapping("/version")
    public Object updateGameVersion(@RequestParam String version) {
        riotDataService.updateGameVersion(version);
        riotDataService.insertChampionData();
        riotDataService.insertSpellData();
        return new ResponseWrapper().build();
    }
}
