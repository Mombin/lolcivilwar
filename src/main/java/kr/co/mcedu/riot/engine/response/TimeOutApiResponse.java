package kr.co.mcedu.riot.engine.response;

import kr.co.mcedu.riot.engine.RiotApiResponseCode;
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
