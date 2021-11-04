package kr.co.mcedu.riot;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class RiotApiResponse {

    private RiotApiResponseCode state = RiotApiResponseCode.DEFAULT;

    public abstract RiotApiResponse convertToResponse(String entityMsg);

}
