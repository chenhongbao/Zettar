package io.zettar.core;

public enum EventType {
    OrderInsertion,
    Subscription,
    OrderStateUpdate,
    TradeUpdate,
    InstrumentStateUpdate,
    SnapshotUpdate,
    Logging,
    SystemInfo,
    SystemWarning,
    SystemError,
    SystemPanic
}
