package kr.co.mcedu.broker;

import kr.co.mcedu.broker.enums.EventType;
import kr.co.mcedu.broker.model.EventPayload;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class EventBroker {

    private final EnumMap<EventType, ConcurrentLinkedQueue<EventPayload>> queue = new EnumMap<>(EventType.class);


    public void pushEvent(EventPayload payload) {
        ConcurrentLinkedQueue<EventPayload> inQueue = queue.getOrDefault(payload.getType(), new ConcurrentLinkedQueue<>());
        inQueue.offer(payload);
        queue.put(payload.getType(), inQueue);
    }

    @Scheduled(fixedRate = 5000)
    private void check() {
        queue.forEach((key, value) -> {
            if (value.isEmpty()) {
                return;
            }
            Runnable innerRunner = key::eventPollerCheck;
            innerRunner.run();
        });
    }

    public Queue<EventPayload> getEventQueue(EventType eventType) {
        return queue.getOrDefault(eventType, new ConcurrentLinkedQueue<>());
    }
}
