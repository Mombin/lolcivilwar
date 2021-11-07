package kr.co.mcedu.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import kr.co.mcedu.group.model.response.CustomUserSynergyResponse;
import kr.co.mcedu.group.model.response.PersonalResultResponse;
import kr.co.mcedu.match.model.response.MatchHistoryResponse;
import lombok.Getter;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;

@Component
@Getter
public class LocalCacheManager {
    private Cache<String, CustomUserSynergyResponse> synergyCache;
    private Cache<String, Map<Integer, MatchHistoryResponse>> matchHistoryCache;
    private Cache<String, HashMap<Integer, PersonalResultResponse>> personalResultHistoryCache;
    private Cache<String, Boolean> userIdCache;
    private Cache<String, Boolean> emailCache;
    private Cache<String, Long> alarmCountCache;

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
    }
}