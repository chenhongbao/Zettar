package io.zettar.core.event;

public enum EventType {
    OrderInsertion,
    Subscription,
    OrderStateUpdate,
    TradeUpdate,
    InstrumentStateUpdate,
    SnapshotUpdate,
    ClientLogging,
    SystemLogging,
    SystemPanic
}
