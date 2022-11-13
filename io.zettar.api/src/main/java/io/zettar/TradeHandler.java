package io.zettar;

@FunctionalInterface
public interface TradeHandler {
    void handle(Trade trade);
}
