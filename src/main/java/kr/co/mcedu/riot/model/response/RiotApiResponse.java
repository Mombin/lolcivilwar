package kr.co.mcedu.riot.model.response;

import kr.co.mcedu.riot.RiotApiResponseCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class RiotApiResponse {

    private RiotApiResponseCode state = RiotApiResponseCode.DEFAULT;

    public abstract RiotApiResponse convertToResponse(String entityMsg);

}
