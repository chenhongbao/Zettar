package io.zettar.core.event;

public record EventOrigin(
        EventOriginType type,
        String originId) {
}
