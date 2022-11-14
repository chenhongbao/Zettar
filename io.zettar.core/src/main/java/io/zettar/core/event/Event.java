package io.zettar.core.event;

import java.time.ZonedDateTime;

public record Event<T>(
        String id,
        String sourceId,
        String groupId,
        EventType type,
        EventSourceType sourceType,
        ZonedDateTime whenCreated,
        T event) {
}
