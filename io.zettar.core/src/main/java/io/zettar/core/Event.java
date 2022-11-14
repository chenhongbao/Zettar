package io.zettar.core;

import java.time.ZonedDateTime;

public record Event(
        EventType type,
        Object event,
        EventSource origin,
        ZonedDateTime timeStamp) {
}
