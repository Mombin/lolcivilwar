package kr.co.mcedu.riot.service.impl;

import kr.co.mcedu.common.property.LolCdnProperty;
import kr.co.mcedu.common.service.CommonService;
import kr.co.mcedu.riot.entity.ChampionDataEntity;
import kr.co.mcedu.riot.entity.SummonerSpellEntity;
import kr.co.mcedu.riot.model.*;
import kr.co.mcedu.riot.repository.ChampionJpaRepository;
import kr.co.mcedu.riot.repository.RiotDataRepository;
import kr.co.mcedu.riot.repository.SummonerSpellJpaRepository;
import kr.co.mcedu.riot.service.RiotDataService;
import kr.co.mcedu.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RiotDataServiceImpl
        implements RiotDataService {
    private final CommonService commonService;
    private final RiotDataRepository riotDataRepository;
    private final ChampionJpaRepository championJpaRepository;
    private final SummonerSpellJpaRepository summonerSpellJpaRepository;

    private final ModelMapper modelMapper = new ModelMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<Long, ChampionCacheData> championMap = new HashMap<>();

    private LolCdnProperty lolCdnProperty;

    @PostConstruct
    public void init() {
        List<ChampionDataEntity> champions = riotDataRepository.getChampions();
        champions.forEach(entity -> championMap.put(entity.getChampionId(), new ChampionCacheData(entity)));
        lolCdnProperty = commonService.getLolVersionProperty();
    }

    @Override
    @Transactional
    public void insertChampionData() {
        RiotJsonResponse response = this.getJsonResponse(RiotJsonData.CHAMPION);
        Map<String, Map<String, Object>> data = response.getData();
        championJpaRepository.findAll();
        List<ChampionDataEntity> entities = data.values().stream()
                                                .map(dataMap -> modelMapper.map(dataMap, ChampionData.class))
                                                .map(ChampionDataEntity::new)
                                                .collect(Collectors.toList());
        championJpaRepository.saveAll(entities);
    }

    @Override
    @Transactional
    public void insertSpellData() {
        RiotJsonResponse response = this.getJsonResponse(RiotJsonData.SPELL);
        Map<String, Map<String, Object>> data = response.getData();
        summonerSpellJpaRepository.findAll();
        List<SummonerSpellEntity> entityList = data.values().stream()
                                                   .map(dataMap -> modelMapper.map(dataMap, SummonerSpellData.class))
                                                   .map(SummonerSpellEntity::new)
                                                   .collect(Collectors.toList());
        summonerSpellJpaRepository.saveAll(entityList);
    }

    private RiotJsonResponse getJsonResponse(RiotJsonData data) {
        String url = lolCdnProperty.getCdnUrl() + data.getJsonUrl();
        return Optional.ofNullable(restTemplate.getForObject(url, RiotJsonResponse.class, Collections.emptyMap())).orElse(new RiotJsonResponse());
    }

    @Override
    @Transactional
    public String getChampionName(Long championId) {
        ChampionCacheData championCacheData = championMap.get(championId);
        if (championCacheData == null) {
            riotDataRepository.getChampion(championId)
                              .ifPresent(championDataEntity -> championMap.put(championId, new ChampionCacheData(championDataEntity)));
            championCacheData = championMap.get(championId);
        }
        return Optional.ofNullable(championCacheData).map(ChampionCacheData::getChampionName).orElse("");
    }

    @Override
    public String getChampionImageUrl(String championName) {
        if (StringUtils.isEmpty(championName) || StringUtils.isEmpty(lolCdnProperty.getVersion())) {
            return "";
        }
        return lolCdnProperty.getCdnUrl() + "/img/champion/" + championName + ".png";
    }

    @Override
    @Transactional
    public String getChampionImageUrlById(Long championId) {
        return this.getChampionImageUrl(this.getChampionName(championId));
    }

    @Override
    public void updateGameVersion(String version) {
        commonService.updateLolVersionProperty(version);
        lolCdnProperty = commonService.getLolVersionProperty();
    }
}
