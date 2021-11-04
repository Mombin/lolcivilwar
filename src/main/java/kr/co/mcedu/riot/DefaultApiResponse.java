package kr.co.mcedu.riot;

public class DefaultApiResponse extends RiotApiResponse{

    private Object DefaultApiResponse;

    @Override
    public RiotApiResponse convertToResponse(String entityMsg) {
        return (RiotApiResponse) DefaultApiResponse;
    }
}
