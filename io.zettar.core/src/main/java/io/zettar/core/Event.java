package io.zettar.core;

import java.time.ZonedDateTime;

public record Event(
        String eventId,
        EventType type,
        Object event,
        EventOrigin origin,
        ZonedDateTime timeStamp) {
}
