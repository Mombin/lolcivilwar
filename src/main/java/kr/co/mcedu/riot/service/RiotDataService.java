package kr.co.mcedu.riot.service;

public interface RiotDataService {
    String getChampionName(Long championId);

    String getChampionImageUrl(String championName);
}
