package kr.co.mcedu.riot;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import kr.co.mcedu.common.service.CommonService;
import kr.co.mcedu.utils.TimeUtils;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class ApiEngine {
    @Autowired
    public CommonService commonService;
    private final static Logger logger = LoggerFactory.getLogger(ApiEngine.class);

    private ApiShooter apiShooter ;

    @Getter
    @Setter
    public static final class ApiShooter extends Thread {
        private LinkedBlockingQueue<RiotApiRequest> request = new LinkedBlockingQueue<>();
        private Cache<String, RiotApiResponse> response = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
        private Boolean state = true;
        private Boolean isValidationProperty = false;


        private void getPropertyErrorResponse(){

        }

        //구현할예정
//        private class ApiShooter(var riotApiProperty: RiotApiProperty) : Thread() {
//            val request : LinkedBlockingQueue<RiotApiRequest> = LinkedBlockingQueue() // blockingQueue >> 인입순서별로 request
//            val response : Cache<String, RiotApiResponse> = CacheBuilder.newBuilder() // response 는 map처럼 key 값으로 분류 String = message
//                    .maximumSize(1000)
//                    .expireAfterWrite(10, TimeUnit.MINUTES)
//                    .build()
//            var state : Boolean = true
//            var isValidationProperty : Boolean = false
//            var engineCount: EngineCount = EngineCount()
//
//            override fun run() {
//                while(state) {
//                    val message = request.take()
//                    if (!engineCount.innerTime.plusSeconds(1).isBefore(LocalDateTime.now())) {
//                        engineCount.innerCount = 0
//                        engineCount.innerTime = LocalDateTime.now()
//                    }
//                    if (!engineCount.outerTime.plusMinutes(2).isBefore(LocalDateTime.now())) {
//                        engineCount.outerCount = 0
//                        engineCount.outerTime = LocalDateTime.now()
//                    }
//
//                    if (engineCount.innerCount >= 20) {
//                        while (!engineCount.innerTime.plusSeconds(1).isBefore(LocalDateTime.now())) {
//                            sleep(100)
//                        }
//                        engineCount.innerCount = 0
//                        engineCount.innerTime = LocalDateTime.now()
//                    }
//
//                    if (engineCount.outerCount >= 100) {
//                        while (!engineCount.outerTime.plusMinutes(2).isBefore(LocalDateTime.now())) {
//                            sleep(100)
//                        }
//                        engineCount.outerCount = 0
//                        engineCount.outerTime = LocalDateTime.now()
//                    }
//                    engineCount.innerCount++
//                    engineCount.outerCount++
//                    if (!isValidationProperty) {
//                        response.put(message.messageKey, this.getPropertyErrorResponse())
//                    } else {
//                        InnerShooter(riotApiProperty, message, response).start()
//                    }
//                }
//            }

        private class EngineCount{
            private int innerCount = 0;
            private LocalDateTime innerTime = LocalDateTime.now();
            private int outerCount = 0;
            private LocalDateTime outerTime = LocalDateTime.now();

        }

        public static final class InnerShooter extends Thread{
            @Override
            public void run() {
                long start =TimeUtils.now();

            }
        }

        //TODO : 구현할예정
//        class InnerShooter(val riotApiProperty: RiotApiProperty, val message: RiotApiRequest, val cache: Cache<String, RiotApiResponse>): Thread() {
//            override fun run() {
//                val start = TimeUtils.now()
//                cache.put(message.messageKey, this.sendRequest(message))
//                logger.info("${message.apiType.msg} ${(TimeUtils.now() - start)}ms")
//            }
//
//            private fun sendRequest(message : RiotApiRequest) : RiotApiResponse {
//                message.build()
//                val request = HttpGet(URIBuilder(riotApiProperty.riotApiUrl + message.url).build()).also {
//                    it.setHeader("X-Riot-Token", riotApiProperty.apiKey)
//                }
//                val response = HttpClientBuilder.create().build().execute(request)
//                val msg = EntityUtils.toString(response.entity)
//                logger.info("response : {}", msg)
//                val responseCode = RiotApiResponseCode.findByState(response.statusLine.statusCode.toString())
//                if (responseCode == RiotApiResponseCode.SUCCESS) {
//                    val clazz = message.apiType.responseType as Class<*>
//                    val riotApiResponse = clazz.getDeclaredConstructor().newInstance() as RiotApiResponse
//                    return riotApiResponse.convertToResponse(msg)
//                }
//                return DefaultApiResponse()
//            }
//        }



    }

}
