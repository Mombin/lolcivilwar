package kr.co.mcedu.broker.model;

import kr.co.mcedu.broker.enums.EventType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventPayload {
    protected EventType type;
    protected EventPayload(EventType type) {
        this.type = type;
    }
}
