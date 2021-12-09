package kr.co.mcedu.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import kr.co.mcedu.common.model.PageWrapper;
import kr.co.mcedu.group.model.response.CustomUserSynergyResponse;
import kr.co.mcedu.group.model.response.GroupInviteHistoryResponse;
import kr.co.mcedu.group.model.response.PersonalResultResponse;
import kr.co.mcedu.match.model.response.MatchHistoryResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;

@Component
@Getter
@Slf4j
public class LocalCacheManager {
    private Cache<String, CustomUserSynergyResponse> synergyCache;
    private Cache<String, Map<Integer, MatchHistoryResponse>> matchHistoryCache;
    private Cache<String, Map<Integer, PersonalResultResponse>> personalResultHistoryCache;
    private Cache<String, Boolean> userIdCache;
    private Cache<String, Boolean> emailCache;
    private Cache<String, Long> alarmCountCache;
    private Cache<String, Map<Integer, PageWrapper<GroupInviteHistoryResponse>>> groupInviteHistoryCache;

    @PostConstruct
    public void after() {
        synergyCache = CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.HOURS)
                .build();
        matchHistoryCache = CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.HOURS)
                .build();
        personalResultHistoryCache = CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.HOURS)
                .build();
        userIdCache = CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.HOURS)
                .build();
        emailCache = CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.HOURS)
                .build();
        alarmCountCache = CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.HOURS)
                .build();
        groupInviteHistoryCache= CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.HOURS)
                .build();
    }

    public void invalidSynergyCache(final String invalidCacheKey) {
        synergyCache.invalidate(invalidCacheKey);
        log.info("Invalid Synergy Cache : {}", invalidCacheKey);
    }

    public void putSynergyCache(String cacheKey, CustomUserSynergyResponse object) {
        synergyCache.put(cacheKey, object);
        log.info("Put Synergy Cache : {}", cacheKey);
    }

    public CustomUserSynergyResponse getSynergy(String cacheKey) {
        return synergyCache.getIfPresent(cacheKey);
    }

    public Map<Integer, MatchHistoryResponse> getMatchHistory(String cacheKey) {
        Map<Integer, MatchHistoryResponse> map = matchHistoryCache.getIfPresent(cacheKey);
        if (map == null) {
            map = new HashMap<>();
        }
        return map;
    }

    public void putMatchHistoryCache(String cacheKey, Map<Integer, MatchHistoryResponse> map) {
        matchHistoryCache.put(cacheKey, map);
        log.info("Put MatchHistory Cache : {}", cacheKey);
    }

    public void invalidMatchHistoryCache(final String cacheKey) {
        matchHistoryCache.invalidate(cacheKey);
        log.info("Invalid MatchHistory Cache : {}", cacheKey);
    }

    public Map<Integer, PersonalResultResponse> getPersonalResultHistory(String cacheKey) {
        Map<Integer, PersonalResultResponse> map = personalResultHistoryCache.getIfPresent(cacheKey);
        if (map == null) {
            map = new HashMap<>();
        }
        return map;
    }

    public void putPersonalResultHistory(String cacheKey, Map<Integer, PersonalResultResponse> map) {
        personalResultHistoryCache.put(cacheKey, map);
        log.info("Put PersonalResultHistory Cache : {}", cacheKey);
    }

    public void invalidAlarmCountCache(String cacheKey) {
        alarmCountCache.invalidate(cacheKey);
        log.info("Invalid AlarmCount Cache : {}", cacheKey);
    }

    public Long getAlarmCount(final String userId) {
        return alarmCountCache.getIfPresent(userId);
    }

    public void putAlarmCountCache(final String userId, final Long count) {
        alarmCountCache.put(userId, count);
        log.info("Put AlarmCount Cache : {}", userId);
    }

    public void putGroupInviteHistoryCache(final String groupSeq, Integer page, PageWrapper<GroupInviteHistoryResponse> pageWrapper) {
        Map<Integer, PageWrapper<GroupInviteHistoryResponse>> groupInviteHistoryCacheMap = getGroupInviteHistoryCache(groupSeq);
        groupInviteHistoryCacheMap.put(page, pageWrapper);
        groupInviteHistoryCache.put(groupSeq, groupInviteHistoryCacheMap);
        log.info("Put GroupInviteHistory Cache : {}, page : {}", groupSeq, page);
    }

    public Map<Integer, PageWrapper<GroupInviteHistoryResponse>> getGroupInviteHistoryCache(String groupSeq) {
        Map<Integer, PageWrapper<GroupInviteHistoryResponse>> map = groupInviteHistoryCache.getIfPresent(groupSeq);
        if (map == null) {
            map = new HashMap<>();
        }
        return map;
    }

    public void invalidGroupInviteHistoryCache(String groupSeq) {
        groupInviteHistoryCache.invalidate(groupSeq);
        log.info("Invalid GroupInviteHistoryCache Cache : {}", groupSeq);
    }

}