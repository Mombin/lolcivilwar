package kr.co.mcedu.riot.data.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.mcedu.common.entity.SystemEntity;
import kr.co.mcedu.common.repository.SystemRepository;
import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.riot.data.entity.ChampionDataEntity;
import kr.co.mcedu.riot.data.entity.SummonerSpellEntity;
import kr.co.mcedu.riot.data.repository.ChampionJpaRepository;
import kr.co.mcedu.riot.data.repository.SummonerSpellJpaRepository;
import kr.co.mcedu.riot.data.service.GameDataService;
import kr.co.mcedu.user.repository.WebUserRepository;
import kr.co.mcedu.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameDataServiceImpl implements GameDataService {

    private final ChampionJpaRepository championJpaRepository;
    private final SummonerSpellJpaRepository summonerSpellJpaRepository;
    private final SystemRepository systemRepository;
    private final WebUserRepository webUserRepository;


    @Override
    public void insertChampionAndSpellData() throws AccessDeniedException {
        String lolcwTag  = SessionUtils.getUserInfo().getLolcwTag();
        String authority = webUserRepository.findWebUserEntityByLolcwTag(lolcwTag).get().getAuthority();
        if(!authority.equals("ADMIN")){
            throw new AccessDeniedException("권한이 없습니다.");
        }
        String ddragonUrl = "https://ddragon.leagueoflegends.com/cdn/";
        String championUrl = "/data/ko_KR/champion.json";
        String spellUrl = "/data/ko_KR/summoner.json";
        String version = systemRepository.findByPropertyName("LOL_VERSION").map(SystemEntity::getPropertyValue1).orElseThrow(() -> new ServiceException("버전을 갱신해주세요"));
        RestTemplate template = new RestTemplate();
        String response = "";

        Map<String, Object> params = new HashMap<>();
        response = template.getForObject(ddragonUrl + version + championUrl,String.class,params);

        String result = response.toString();

        String response2 = "";

        Map<String, Object> params2 = new HashMap<>();
        response2 = template.getForObject(ddragonUrl + version + spellUrl,String.class,params2);

        String result2 = response2.toString();
        try {

            JSONObject responseJson = new JSONObject(result);
            JSONObject responseJson2 = new JSONObject(result2);
            JSONObject championJson = responseJson.getJSONObject("data");
            JSONObject spellJson = responseJson2.getJSONObject("data");
            List<LinkedHashMap<String,Object>> championList = new ArrayList<>();
            List<LinkedHashMap<String,Object>> spellList = new ArrayList<>();

            Map<String,LinkedHashMap<String,Object>> champMap = new ObjectMapper().readValue(championJson.toString(),Map.class);

            for(String key : champMap.keySet()){
                championList.add(champMap.get(key));
            }
            Map<String,LinkedHashMap<String,Object>> spellMap = new ObjectMapper().readValue(spellJson.toString(),Map.class);

            for(String key : spellMap.keySet()){
                spellList.add(spellMap.get(key));
            }
            for (LinkedHashMap<String, Object> objectLinkedHashMap : championList) {
                ChampionDataEntity championDataEntity = new ChampionDataEntity();
                championDataEntity.setChampionId(Long.parseLong(objectLinkedHashMap.get("key").toString()));
                championDataEntity.setChampionName(objectLinkedHashMap.get("id").toString());
                championJpaRepository.save(championDataEntity);
            }
            for (LinkedHashMap<String, Object> stringObjectLinkedHashMap : spellList) {
                SummonerSpellEntity summonerSpellEntity = new SummonerSpellEntity();
                summonerSpellEntity.setSummonerSpellId(Long.parseLong(stringObjectLinkedHashMap.get("key").toString()));
                summonerSpellEntity.setSummonerSpellName(stringObjectLinkedHashMap.get("id").toString());
                summonerSpellJpaRepository.save(summonerSpellEntity);
            }

        } catch (IOException e) {
            log.error("IOException ::{}", e.getMessage());
        } catch (JSONException e) {
            log.error("JSONException :::{}",e.getMessage());
        }
    }
}
