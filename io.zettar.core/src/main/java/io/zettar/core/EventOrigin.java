package io.zettar.core;

public record EventOrigin(
        OriginType type,
        String sourceId) {
}
