package kr.co.mcedu.broker.model;

import kr.co.mcedu.broker.enums.EventType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshSummonerEvent extends EventPayload {
    private String accountId;
    public RefreshSummonerEvent(String accountId) {
        super(EventType.REFRESH_SUMMONER);
        this.accountId = accountId;
    }
}
