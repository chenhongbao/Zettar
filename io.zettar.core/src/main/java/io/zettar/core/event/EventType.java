package io.zettar.core.event;

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
