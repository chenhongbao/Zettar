package io.zettar.core.quick;

import io.zettar.InstrumentState;
import io.zettar.OrderState;
import io.zettar.Snapshot;
import io.zettar.Trade;
import io.zettar.core.event.Event;
import io.zettar.core.event.EventListener;

import java.util.Collection;
import java.util.HashSet;

class QuickHandlerWrapper implements EventListener {
    private final HashSet<String> subscribe = new HashSet<>();
    private final QuickHandler client;
    private final QuickImpl impl;

    QuickHandlerWrapper(QuickImpl quick, QuickHandler handler, Collection<String> instrumentId) {
        impl = quick;
        client = handler;
        subscribe.addAll(instrumentId);
    }

    @Override
    public void listen(Event<?> event) {
        switch (event.type()) {
            case SnapshotUpdate -> {
                Snapshot snap = (Snapshot) event.event();
                impl.update(snap);
                if (subscribe.contains(snap.instrumentId())) {
                    client.handle(snap, impl);
                }
            }
            case InstrumentStateUpdate -> impl.update((InstrumentState) event.event());
            case OrderStateUpdate -> impl.update((OrderState) event.event());
            case TradeUpdate -> impl.update((Trade) event.event());
            default -> throw new UnsupportedOperationException("Unsupported event type: " + event.type() + ".");
        }
    }
}
