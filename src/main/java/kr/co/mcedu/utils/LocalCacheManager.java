package kr.co.mcedu.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import kr.co.mcedu.group.model.response.CustomUserSynergyResponse;
import lombok.Getter;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;

@Component
@Getter
public class LocalCacheManager {
    private Cache<String, CustomUserSynergyResponse> synergyCache;
//    lateinit var matchHistoryCache : Cache<String, HashMap<Int, MatchHistoryResponse>>
//    lateinit var personalResultHistoryCache : Cache<String, HashMap<Int, PersonalResultResponse>>
    private Cache<String, Boolean> userIdCache;
    private Cache<String, Boolean> emailCache;
    private Cache<String, Long> alarmCountCache;

    @PostConstruct
    public void after() {
        synergyCache = CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.HOURS)
                .build();
//        matchHistoryCache = CacheBuilder.newBuilder()
//                .expireAfterAccess(1, TimeUnit.HOURS)
//                .build()
//        personalResultHistoryCache = CacheBuilder.newBuilder()
//                .expireAfterAccess(1, TimeUnit.HOURS)
//                .build()
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