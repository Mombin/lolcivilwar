package kr.co.mcedu.summoner.service;

import kr.co.mcedu.summoner.entity.SummonerEntity;
import kr.co.mcedu.summoner.model.response.SummonerResponse;

public interface SummonerService {
    SummonerResponse getSummonerInCache(String summonerName);
    SummonerEntity findByAccountId(String accountId);
    SummonerEntity getSummoner(String summonerName, String accountId);
}