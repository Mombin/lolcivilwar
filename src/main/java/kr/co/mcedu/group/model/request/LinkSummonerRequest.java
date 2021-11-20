package kr.co.mcedu.group.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LinkSummonerRequest {
    private Long groupSeq;
    private Long userSeq;
    private String accountId;

    public LinkSummonerRequest() {
        this.groupSeq = 0L;
        this.userSeq = 0L;
        this.accountId = "";
    }
}