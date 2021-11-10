package kr.co.mcedu.riot.model.response;


public class DefaultApiResponse extends RiotApiResponse {
    @Override
    public RiotApiResponse convertToResponse(String entityMsg) {
        return new DefaultApiResponse();
    }
}
