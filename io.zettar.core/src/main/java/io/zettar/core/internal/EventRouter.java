package io.zettar.core.internal;

import io.zettar.*;
import io.zettar.core.EventListener;

public class EventRouter implements TradeHandler, SnapshotHandler, OrderStateHandler, InstrumentStateHandler {
    @Override
    public void handle(InstrumentState state) {

    }

    @Override
    public void handle(OrderState state) {

    }

    @Override
    public void handle(Snapshot snapshot) {

    }

    @Override
    public void handle(Trade trade) {

    }

    public void addListener(EventListener listener) {

    }

    public void setSource(Source source) {

    }
}
