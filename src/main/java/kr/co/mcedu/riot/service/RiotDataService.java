package kr.co.mcedu.riot.service;

public interface RiotDataService {
    void insertChampionData();

    void insertSpellData();

    String getChampionName(Long championId);

    String getChampionImageUrl(String championName);
}
