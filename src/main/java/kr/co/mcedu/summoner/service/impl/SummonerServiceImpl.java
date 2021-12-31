package kr.co.mcedu.summoner.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import kr.co.mcedu.config.exception.DataNotExistException;
import kr.co.mcedu.group.entity.CustomUserEntity;
import kr.co.mcedu.group.repository.CustomUserRepository;
import kr.co.mcedu.riot.engine.ApiEngine;
import kr.co.mcedu.riot.engine.RiotApiRequest;
import kr.co.mcedu.riot.engine.RiotApiType;
import kr.co.mcedu.riot.engine.response.RiotApiResponse;
import kr.co.mcedu.riot.engine.response.SummonerNameSearchResponse;
import kr.co.mcedu.summoner.entity.SummonerEntity;
import kr.co.mcedu.summoner.model.response.SummonerResponse;
import kr.co.mcedu.summoner.repository.SummonerRepository;
import kr.co.mcedu.summoner.service.SummonerService;
import kr.co.mcedu.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SummonerServiceImpl implements SummonerService {
    private final ApiEngine apiEngine;
    private final SummonerRepository summonerRepository;
    private final CustomUserRepository customUserRepository;

    private final LoadingCache<String, SummonerResponse> summonerCache = CacheBuilder.newBuilder().expireAfterWrite(10,
            TimeUnit.MINUTES).build(CacheLoader.from(summonerName -> {
        if (summonerName != null) {
            SummonerEntity summonerEntity = this.getSummonerBySummonerName(summonerName);
            if (summonerEntity != null) {
                return SummonerResponse.convert(summonerEntity);
            }
        }
        return null;
    }));


    @Override
    public SummonerResponse getSummonerInCache(final String summonerName) {
        SummonerResponse summonerResponse = null;
        try {
            summonerResponse = summonerCache.get(summonerName);
        } catch (Exception ignore) {
        }
        return summonerResponse;
    }

    @Override
    public SummonerEntity findByAccountId(final String accountId) throws DataNotExistException {
        Optional<SummonerEntity> summoner = summonerRepository.findById(accountId);
        return summoner.orElseThrow(DataNotExistException::new);
    }

    @Override
    public SummonerEntity getSummonerByAccountId(String accountId) {
        if (StringUtils.isEmpty(accountId)) {
            return null;
        }
        Optional<SummonerEntity> result = summonerRepository.findById(accountId);
        if (result.isPresent()) {
            SummonerEntity summonerEntity = result.get();

            LocalDateTime modifiedDate = summonerEntity.getModifiedDate();
            if (modifiedDate == null) {
                return this.refresh(summonerEntity);
            }

            if (modifiedDate.plusHours(1).isBefore(LocalDateTime.now())) {
                return this.refresh(summonerEntity);
            }
            return summonerEntity;
        }
        return null;
    }

    @Override
    @Transactional
    public SummonerEntity getSummonerBySummonerName(String summonerName) {
        String search = summonerName.replace("\\s", "").toUpperCase();
        if (StringUtils.isEmpty(search)) {
            return null;
        }
        Optional<SummonerEntity> result = summonerRepository.findSummonerEntityBySearchName(search);

        if (result.isPresent()) {
            SummonerEntity summonerEntity = result.get();

            LocalDateTime modifiedDate = summonerEntity.getModifiedDate();
            if (modifiedDate == null) {
                return this.refresh(summonerEntity);
            }

            if (modifiedDate.plusHours(1).isBefore(LocalDateTime.now())) {
                return this.refresh(summonerEntity);
            }
            return summonerEntity;
        }
        return requestByName(summonerName.toUpperCase());
    }

    /**
     * 소환사명으로 검색
     */
    private SummonerEntity requestByName(String summonerName) {
        RiotApiRequest riotApiRequest = new RiotApiRequest();
        riotApiRequest.setApiType(RiotApiType.SUMMONER);
        riotApiRequest.getData().put("summonerName", summonerName);

        RiotApiResponse response = apiEngine.sendRequest(riotApiRequest);
        if (response instanceof SummonerNameSearchResponse) {
            return summonerRepository.save(((SummonerNameSearchResponse) response).toEntity());
        }
        return null;
    }

    /**
     * EncryptedAccountId로 refresh
     */
    private SummonerEntity refresh(SummonerEntity entity) {
        RiotApiRequest riotApiRequest = new RiotApiRequest();
        riotApiRequest.setApiType(RiotApiType.SUMMONER_BY_ENCRYPTED_ACCOUNT_ID);
        riotApiRequest.getData().put("encryptedAccountId", entity.getAccountId());

        RiotApiResponse response = apiEngine.sendRequest(riotApiRequest);

        if (response instanceof SummonerNameSearchResponse) {
            SummonerEntity summonerEntity = ((SummonerNameSearchResponse) response).toEntity();
            if (!entity.getName().equals(summonerEntity.getName())) {
                List<CustomUserEntity> list = customUserRepository.findAllBySummonerEntityAccountId(summonerEntity.getAccountId());
                list.forEach(customUserEntity -> customUserEntity.setSummonerName(summonerEntity.getName()));
                customUserRepository.saveAll(list);
            }
            entity.setName(summonerEntity.getName());
            entity.setId(summonerEntity.getId());
            entity.setModifiedDate(LocalDateTime.now());
            entity.setProfileIconId(summonerEntity.getProfileIconId());
            entity.setSummonerLevel(summonerEntity.getSummonerLevel());
            entity.setSearchName(summonerEntity.getName().replace("\\s", "").toUpperCase());
            return summonerRepository.save(entity);
        }
        return null;
    }
}
