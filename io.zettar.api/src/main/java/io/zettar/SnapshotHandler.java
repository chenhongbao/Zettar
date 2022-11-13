package io.zettar;

@FunctionalInterface
public interface SnapshotHandler {
    void handle(Snapshot snapshot);
}
