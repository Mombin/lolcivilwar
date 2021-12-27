package kr.co.mcedu.riot.data.model;

import lombok.Getter;

@Getter
public enum RiotJsonData {
    SPELL("/data/ko_KR/summoner.json"), CHAMPION("/data/ko_KR/champion.json");

    private final String jsonUrl;

    RiotJsonData(String jsonUrl) {
        this.jsonUrl = jsonUrl;
    }
}
