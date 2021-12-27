package kr.co.mcedu.riot.engine.response;

public class DefaultApiResponse extends RiotApiResponse {
    @Override
    public RiotApiResponse convertToResponse(String entityMsg) {
        return new DefaultApiResponse();
    }
}
