package io.zettar.core.quick;

import io.zettar.Snapshot;

@FunctionalInterface
public interface QuickHandler {
    void handle(Snapshot snapshot, Quick quick);
}
