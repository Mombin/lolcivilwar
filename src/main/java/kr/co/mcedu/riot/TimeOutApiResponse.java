package kr.co.mcedu.riot;

import kr.co.mcedu.riot.model.response.RiotApiResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeOutApiResponse extends RiotApiResponse {

    private RiotApiResponseCode state = RiotApiResponseCode.TIMEOUT;

    @Override
    public RiotApiResponse convertToResponse(String entityMsg) {
        return new TimeOutApiResponse();
    }
}
