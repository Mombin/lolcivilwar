package kr.co.mcedu.riot.model.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.mcedu.riot.RiotApiResponseCode;
import kr.co.mcedu.summoner.entity.SummonerEntity;
import kr.co.mcedu.utils.ModelUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    public SummonerEntity toEntity() {
        SummonerEntity summonerEntity = ModelUtils.map(this, SummonerEntity.class);
        summonerEntity.setSearchName(this.name.replace("\\s", "").toUpperCase());
        return summonerEntity;
    }
}
