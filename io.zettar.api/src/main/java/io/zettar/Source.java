package io.zettar;

public interface Source  {
    void insert(Order order);
    void subscribe(Subscription subscription);
    void handledBy(OrderStateHandler stateHandler);
    void handledBy(TradeHandler tradeHandler);
    void handledBy(SnapshotHandler snapshotHandler);
    void handledBy(InstrumentStateHandler instrumentHandler);
}
