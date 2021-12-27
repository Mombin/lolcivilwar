package kr.co.mcedu.riot.data.service.impl;

import kr.co.mcedu.common.property.LolCdnProperty;
import kr.co.mcedu.common.service.CommonService;
import kr.co.mcedu.riot.data.entity.ChampionDataEntity;
import kr.co.mcedu.riot.data.entity.SummonerSpellEntity;
import kr.co.mcedu.riot.data.model.ChampionData;
import kr.co.mcedu.riot.data.model.RiotJsonData;
import kr.co.mcedu.riot.data.model.RiotJsonResponse;
import kr.co.mcedu.riot.data.model.SummonerSpellData;
import kr.co.mcedu.riot.data.repository.ChampionJpaRepository;
import kr.co.mcedu.riot.data.repository.SummonerSpellJpaRepository;
import kr.co.mcedu.riot.data.service.GameDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameDataServiceImpl implements GameDataService {

    private final CommonService commonService;
    private final ChampionJpaRepository championJpaRepository;
    private final SummonerSpellJpaRepository summonerSpellJpaRepository;

    private final ModelMapper modelMapper = new ModelMapper();
    private final RestTemplate restTemplate = new RestTemplate();

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
        LolCdnProperty lolVersionProperty = commonService.getLolVersionProperty();
        String url = lolVersionProperty.getCdnUrl() + data.getJsonUrl();
        return restTemplate.getForObject(url, RiotJsonResponse.class, Collections.emptyMap());
    }
}
