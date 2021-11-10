package kr.co.mcedu.riot.model.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.mcedu.riot.RiotApiResponseCode;
import lombok.Getter;

@Getter
public class SummonerNameSearchResponse extends RiotApiResponse {

    private RiotApiResponseCode state = RiotApiResponseCode.SUCCESS;
    private String id ;
    private String accountId ;
    private String puuid ;
    private String name ;
    private Integer profileIconId ;
    private Long revisionDate ;
    private Integer summonerLevel;


    @Override
    public RiotApiResponse convertToResponse(String entityMsg) {
        try {
            return new ObjectMapper().readValue(entityMsg, this.getClass());
        } catch (JsonProcessingException e) {
            DefaultApiResponse defaultApiResponse = new DefaultApiResponse();
            defaultApiResponse.setState(RiotApiResponseCode.PARSING_ERROR);
            return defaultApiResponse;
        }
    }
}
