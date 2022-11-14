package io.zettar.core;

public enum EventType {
    OrderInsertion,
    OrderDeletion,
    OrderStateUpdate,
    TradeUpdate,
    InstrumentStateUpdate,
    Subscription,
    UnSubscription,
    SnapshotUpdate,
    Logging,
    SystemInfo,
    SystemWarning,
    SystemError,
    SystemPanic
}
