package io.zettar.core.quick;

import io.zettar.*;
import io.zettar.core.event.*;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;

class OrderInsertListener implements EventListener {
    private final Source src;
    private final EventRouter evt;

    OrderInsertListener(Source source, EventRouter router) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(router);
        src = source;
        evt = router;
    }

    @Override
    public void listen(Event<?> event) {
        Objects.requireNonNull(event);
        if (event.type() == EventType.OrderInsertion) {
            try {
                src.insert((Order) event.event());
            } catch (SourceException exception) {
                evt.publish(new Event<>(EventUtils.getEventId(), EventUtils.getInstanceId(this), event.groupId(), EventType.OrderStateUpdate, EventSourceType.SourceInput, ZonedDateTime.now(), new OrderState(((Order) event.event()).orderId(), ZonedDateTime.now(), Date.from(Instant.ofEpochMilli(0)), 0, 0, exception.getCode(), 0, false, exception.getMessage())));
            }
        } else {
            throw new UnsupportedOperationException("Unsupported event type: " + event.type() + ".");
        }
    }
}
