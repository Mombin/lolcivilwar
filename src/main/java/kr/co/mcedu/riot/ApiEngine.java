package kr.co.mcedu.riot;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import kr.co.mcedu.common.service.CommonService;
import kr.co.mcedu.riot.model.response.DefaultApiResponse;
import kr.co.mcedu.riot.model.response.RiotApiResponse;
import kr.co.mcedu.utils.StringUtils;
import kr.co.mcedu.utils.TimeUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiEngine {
    private final CommonService commonService;
    private ApiShooter apiShooter;

    @PostConstruct
    private void init() {
        RiotApiProperty riotApiProperty = commonService.getRiotApiProperty();
        this.apiShooter = new ApiShooter(riotApiProperty);
        this.apiShooter.validationCheck();
        this.apiShooter.start();
    }

    public RiotApiResponse sendRequest(RiotApiRequest request) {
        String messageKey = request.getMessageKey();
        this.apiShooter.request.offer(request);
        long startTime = TimeUtils.now();
        while(System.currentTimeMillis() - startTime < 10 * TimeUtils.SECONDS_AS_MILLISECONDS) {
            final RiotApiResponse result = this.apiShooter.response.getIfPresent(messageKey);
            if (result != null) {
                return result;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignore) {
            }
        }
        return new TimeOutApiResponse();
    }

    public void updateApiProperty(RiotApiProperty riotApiProperty) {
        this.apiShooter.setRiotApiProperty(riotApiProperty);
        log.info("key update 완료");
    }

    @Getter
    @Setter
    private static class ApiShooter extends Thread {
        private LinkedBlockingQueue<RiotApiRequest> request = new LinkedBlockingQueue<>();
        private Cache<String, RiotApiResponse> response = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
        private boolean state = true;
        private boolean isValidationProperty = false;
        private RiotApiProperty riotApiProperty;
        private EngineCount engineCount = new EngineCount();

        public ApiShooter(RiotApiProperty riotApiProperty) {
            this.riotApiProperty = riotApiProperty;
        }

        @Override
        public void run() {
            while (state) {
                try {
                    RiotApiRequest message = request.take();
                    if (!engineCount.innerTime.plusSeconds(1).isBefore(LocalDateTime.now())) {
                        engineCount.innerCount = 0;
                        engineCount.innerTime = LocalDateTime.now();
                    }
                    if (!engineCount.outerTime.plusMinutes(2).isBefore(LocalDateTime.now())) {
                        engineCount.outerCount = 0;
                        engineCount.outerTime = LocalDateTime.now();
                    }
                    if (engineCount.innerCount >= 20) {
                        while (!engineCount.innerTime.plusSeconds(1).isBefore(LocalDateTime.now())) {
                            sleep(100);
                        }
                        engineCount.innerCount = 0;
                        engineCount.innerTime = LocalDateTime.now();
                    }
                    if (engineCount.outerCount >= 100) {
                        while (!engineCount.outerTime.plusMinutes(2).isBefore(LocalDateTime.now())) {
                            sleep(100);
                        }
                        engineCount.outerCount = 0;
                        engineCount.outerTime = LocalDateTime.now();

                    }
                    engineCount.innerCount++;
                    engineCount.outerCount++;
                    if (!isValidationProperty) {
                        this.response.put(message.getMessageKey(), this.getPropertyErrorResponse());
                    } else {
                        new InnerShooter(riotApiProperty, message, response).start();
                    }
                } catch (InterruptedException ignore) {
                }
            }
        }

        public void validationCheck() {
            isValidationProperty = !StringUtils.isEmpty(riotApiProperty.getApiKey()) && !StringUtils.isEmpty(riotApiProperty.getRiotApiUrl());
        }

        private DefaultApiResponse getPropertyErrorResponse() {
            DefaultApiResponse defaultApiResponse = new DefaultApiResponse();
            defaultApiResponse.setState(RiotApiResponseCode.PROPERTY_ERROR);
            return defaultApiResponse;
        }

        private static class EngineCount {
            private int innerCount = 0;
            private LocalDateTime innerTime = LocalDateTime.now();
            private int outerCount = 0;
            private LocalDateTime outerTime = LocalDateTime.now();

        }

        private static class InnerShooter extends Thread {
            private final RiotApiProperty riotApiProperty;
            private final RiotApiRequest message;
            private final Cache<String, RiotApiResponse> cache;

            public InnerShooter(RiotApiProperty riotApiProperty, RiotApiRequest message, Cache<String, RiotApiResponse> cache)  {
                this.riotApiProperty = riotApiProperty;
                this.message = message;
                this.cache = cache;
            }

            @Override
            public void run() {
                long start = TimeUtils.now();
                try {
                    cache.put(message.getMessageKey(), this.sendRequest(message));
                } catch (Exception e) {
                    log.error("", e);
                }
                log.info(this.message.getApiType().getMsg() + " " + (TimeUtils.now() - start) + "ms");
            }

            private RiotApiResponse sendRequest(RiotApiRequest message) throws Exception {
                message.build();
                HttpGet request = new HttpGet(new URIBuilder(riotApiProperty.getRiotApiUrl() + message.getUrl()).build());
                request.setHeader("X-Riot-Token", riotApiProperty.getApiKey());
                CloseableHttpResponse response = HttpClientBuilder.create().build().execute(request);
                String msg = EntityUtils.toString(response.getEntity());
                log.info("response : {}", msg);
                RiotApiResponseCode responseCode = RiotApiResponseCode.findByState(String.valueOf(response.getStatusLine().getStatusCode()));
                if (responseCode == RiotApiResponseCode.SUCCESS) {
                    Class<RiotApiResponse> responseType = (Class<RiotApiResponse>)  message.getApiType().getResponseType();
                    return responseType.getDeclaredConstructor().newInstance().convertToResponse(msg);
                }
                return new DefaultApiResponse();
            }
        }
    }
}
