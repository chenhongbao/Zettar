package io.zettar.core;

import io.zettar.Snapshot;

@FunctionalInterface
public interface QuickHandler {
    void handle(Snapshot snapshot, Quick quick);
}
