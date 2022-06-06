package kr.co.mcedu.broker.enums;

import kr.co.mcedu.summoner.service.impl.SummonerServiceImpl;
import lombok.Getter;

@Getter
public enum EventType {
    REFRESH_SUMMONER(SummonerServiceImpl::executeRefreshEvent);
    private final Runnable eventPollerCheck;

    EventType(final Runnable executeRefreshEvent) {
        this.eventPollerCheck = executeRefreshEvent;
    }

    public void eventPollerCheck() {
        eventPollerCheck.run();
    }
}
