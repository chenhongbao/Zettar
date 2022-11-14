package io.zettar.core.internal;

import io.zettar.*;
import io.zettar.core.EventListener;
import io.zettar.core.Quick;

import java.util.Collection;
import java.util.HashSet;

class QuickImpl implements Quick, TradeHandler, SnapshotHandler, OrderStateHandler, InstrumentStateHandler {
    private final HashSet<EventListener> clients = new HashSet<>();
    private final Source src;

    QuickImpl(Source source, Collection<EventListener> listeners) {
        src = source;
        clients.addAll(listeners);
    }

    @Override
    public void buyOpen(String exchangeId, String instrumentId, long quantity) {

    }

    @Override
    public void buyClose(String exchangeId, String instrumentId, long quantity) {

    }

    @Override
    public void sellOpen(String exchangeId, String instrumentId, long quantity) {

    }

    @Override
    public void sellClose(String exchangeId, String instrumentId, long quantity) {

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

    @Override
    public void handle(InstrumentState state) {

    }
}
