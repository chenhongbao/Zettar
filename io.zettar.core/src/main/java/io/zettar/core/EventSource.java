package io.zettar.core;

public record EventSource(
        EventSourceType type,
        String sourceId) {
}
