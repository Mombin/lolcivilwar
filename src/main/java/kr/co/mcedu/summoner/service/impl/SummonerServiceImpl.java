package kr.co.mcedu.summoner.service.impl;

import kr.co.mcedu.summoner.entity.SummonerEntity;
import kr.co.mcedu.summoner.model.response.SummonerResponse;
import kr.co.mcedu.summoner.service.SummonerService;
import org.springframework.stereotype.Service;

@Service
public class SummonerServiceImpl implements SummonerService {
    @Override public SummonerResponse getSummonerInCache(final String summonerName) {
        return null;
    }

    @Override public SummonerEntity findByAccountId(final String accountId) {
        return null;
    }

    @Override public SummonerEntity getSummoner(final String summonerName, final String accountId) {
        return null;
    }
}
