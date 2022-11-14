package io.zettar.core;

public record EventOrigin(
        EventOriginType type,
        String originId) {
}
