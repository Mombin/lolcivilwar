package kr.co.mcedu.riot.engine.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.mcedu.riot.engine.RiotApiResponseCode;
import kr.co.mcedu.riot.engine.model.BanChampion;
import kr.co.mcedu.riot.engine.model.Participant;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentGameInfoResponse extends RiotApiResponse {

    private RiotApiResponseCode state = RiotApiResponseCode.SUCCESS;
    private List<Participant> participants = new ArrayList<>();
    private Long gameId;
    private List<BanChampion> bannedChampions = new ArrayList<>();

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
