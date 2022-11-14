package io.zettar.core.quick;

import io.zettar.*;
import io.zettar.core.event.*;

import java.time.ZonedDateTime;
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
                evt.publish(new Event<>(EventUtils.getEventId(), event.groupId(), EventType.OrderStateUpdate, new EventOrigin(EventOriginType.SourceInput, EventUtils.getInstanceId(this)), ZonedDateTime.now(), new OrderState(((Order) event.event()).orderId(), ZonedDateTime.now(), null, 0, 0, exception.getCode(), 0, false, exception.getMessage())));
            }
        } else {
            throw new UnsupportedOperationException("Unsupported event type: " + event.type() + ".");
        }
    }
}
