package kr.co.mcedu.riot.data.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.mcedu.common.repository.SystemRepository;
import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.riot.data.entity.ChampionDataEntity;
import kr.co.mcedu.riot.data.entity.SummonerSpellEntity;
import kr.co.mcedu.riot.data.repository.ChampionJpaRepository;
import kr.co.mcedu.riot.data.repository.SummonerSpellJpaRepository;
import kr.co.mcedu.riot.data.service.ChampionDataService;
import kr.co.mcedu.user.repository.WebUserRepository;
import kr.co.mcedu.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChampionDataServiceImpl implements ChampionDataService {

    private final ChampionJpaRepository championJpaRepository;
    private final SummonerSpellJpaRepository summonerSpellJpaRepository;
    private final SystemRepository systemRepository;
    private final WebUserRepository webUserRepository;


    @Override
    public void insertChampionData() throws AccessDeniedException {
        String lolcwTag  = SessionUtils.getUserInfo().getLolcwTag();
        String Authroity = webUserRepository.findWebUserEntityByLolcwTag(lolcwTag).get().getAuthority();
        if(!Authroity.equals("ADMIN")){
            throw new AccessDeniedException("권한이 없습니다.");
        }
        String ddragonUrl = "https://ddragon.leagueoflegends.com/cdn/";
        String championUrl = "/data/ko_KR/champion.json";
        String spellUrl = "/data/ko_KR/summoner.json";
        String version = systemRepository.findByPropertyName("LOL_VERSION").get().getPropertyValue1();
        HttpURLConnection conn = null;
        HttpURLConnection conn2 = null;

        try {
            //URL 설정
            URL url = new URL(ddragonUrl + version + championUrl);
            URL url2 = new URL(ddragonUrl + version + spellUrl);

            conn = (HttpURLConnection) url.openConnection();
            conn2 = (HttpURLConnection) url2.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            BufferedReader br2 = new BufferedReader(new InputStreamReader(conn2.getInputStream(), "UTF-8"));
            StringBuilder sb2 = new StringBuilder();
            String line2 = "";
            while ((line2 = br2.readLine()) != null) {
                sb2.append(line2);
            }
            JSONObject responseJson = new JSONObject(sb.toString());
            JSONObject responseJson2 = new JSONObject(sb2.toString());
            JSONObject championJson = new JSONObject(responseJson.get("data").toString());
            JSONObject spellJson = new JSONObject(responseJson2.get("data").toString());
            List<LinkedHashMap<String,Object>> championList = new ArrayList<>();
            List<LinkedHashMap<String,Object>> spellList = new ArrayList<>();

            Map<String,LinkedHashMap<String,Object>> champMap = null;

            champMap = new ObjectMapper().readValue(championJson.toString(),Map.class);
            for(String key : champMap.keySet()){
                championList.add(champMap.get(key));
            }
            Map<String,LinkedHashMap<String,Object>> spellMap = null;

            spellMap = new ObjectMapper().readValue(spellJson.toString(),Map.class);
            for(String key : spellMap.keySet()){
                spellList.add(spellMap.get(key));
            }
            for (int i = 0; i < championList.size(); i++) {
                ChampionDataEntity championDataEntity = new ChampionDataEntity();
                championDataEntity.setChampionId(Long.parseLong(championList.get(i).get("key").toString()));
                championDataEntity.setChampionName(championList.get(i).get("name").toString());
                championJpaRepository.save(championDataEntity);
            }
            for (int i = 0; i < spellList.size(); i++) {
                SummonerSpellEntity summonerSpellEntity = new SummonerSpellEntity();
                summonerSpellEntity.setSummonerSpellId(Long.parseLong(spellList.get(i).get("key").toString()));
                summonerSpellEntity.setSummonerSpellName(spellList.get(i).get("name").toString());
                summonerSpellJpaRepository.save(summonerSpellEntity);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("not JSON Format response");
            e.printStackTrace();
        }
    }
}
