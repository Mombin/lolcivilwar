package kr.co.mcedu.riot.service.impl;

import kr.co.mcedu.common.property.LolCdnProperty;
import kr.co.mcedu.common.service.CommonService;
import kr.co.mcedu.riot.data.entity.ChampionDataEntity;
import kr.co.mcedu.riot.repository.RiotDataRepository;
import kr.co.mcedu.riot.service.RiotDataService;
import kr.co.mcedu.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RiotDataServiceImpl
        implements RiotDataService {
    private final CommonService commonService;
    private final RiotDataRepository riotDataRepository;
    private final Map<Long, String> championMap = new HashMap<>();
    private LolCdnProperty lolCdnProperty;

    @PostConstruct
    public void init() {
        List<ChampionDataEntity> champions = riotDataRepository.getChampions();
        champions.forEach(entity -> championMap.put(entity.getChampionId(), entity.getChampionName()));
        lolCdnProperty = commonService.getLolVersionProperty();
    }

    @Override
    @Transactional
    public String getChampionName(Long championId) {
        String championName = championMap.getOrDefault(championId, "");
        if (StringUtils.isEmpty(championName)) {
            riotDataRepository.getChampion(championId)
                              .ifPresent(championDataEntity -> championMap.put(championId, championDataEntity.getChampionName()));
            championName = championMap.getOrDefault(championId, "");
        }
        return championName;
    }

    @Override
    public String getChampionImageUrl(String championName) {
        if (StringUtils.isEmpty(championName) || StringUtils.isEmpty(lolCdnProperty.getVersion())) {
            return "";
        }
        return lolCdnProperty.getCdnUrl() + "/img/champion/" + championName + ".png";
    }
}
