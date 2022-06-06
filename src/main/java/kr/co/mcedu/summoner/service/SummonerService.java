package kr.co.mcedu.summoner.service;

import kr.co.mcedu.config.exception.DataNotExistException;
import kr.co.mcedu.summoner.entity.SummonerEntity;
import kr.co.mcedu.summoner.model.response.SummonerResponse;

public interface SummonerService {
    SummonerResponse getSummonerInCache(String summonerName);
    SummonerEntity findByAccountId(String accountId) throws DataNotExistException;
    void refreshSummonerByAccountId(String accountId);
    SummonerEntity getSummonerBySummonerName(String summonerName);
    void executeRefreshEventPolling();
}