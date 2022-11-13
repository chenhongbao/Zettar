package io.zettar;

public interface Source  {
    void insert(Order order, int options);
    void subscribe(Subscription subscription, int options);
    void handledBy(OrderStateHandler stateHandler);
    void handledBy(TradeHandler tradeHandler);
    void handledBy(SnapshotHandler snapshotHandler);
    void handledBy(InstrumentStateHandler instrumentHandler);
}
