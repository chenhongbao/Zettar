package io.zettar;

@FunctionalInterface
public interface InstrumentStateHandler {
    void handle(InstrumentState state);
}
