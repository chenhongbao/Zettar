package io.zettar;

@FunctionalInterface
public interface OrderStateHandler {
    void handle(OrderState state);
}
