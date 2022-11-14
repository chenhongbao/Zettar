package io.zettar.core.event;

import java.time.ZonedDateTime;

public record Event<T>(
        String eventId,
        String groupId,
        EventType type,
        EventOrigin origin,
        ZonedDateTime whenCreated,
        T event) {
}