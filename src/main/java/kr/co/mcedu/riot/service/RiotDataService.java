package kr.co.mcedu.riot.service;

import org.springframework.transaction.annotation.Transactional;

public interface RiotDataService {
    void insertChampionData();

    void insertSpellData();

    String getChampionName(Long championId);

    String getChampionImageUrl(String championName);

    @Transactional String getChampionImageUrlById(Long championId);

    void updateGameVersion(String version);
}
