package io.zettar.core.quick;

import io.zettar.InstrumentState;
import io.zettar.OrderState;
import io.zettar.Snapshot;
import io.zettar.Trade;
import io.zettar.core.event.Event;
import io.zettar.core.event.EventListener;

import java.util.Collection;

class QuickHandlerWrapper implements EventListener {
    private final QuickHandler client;
    private final QuickImpl impl;

    QuickHandlerWrapper(QuickImpl quick, QuickHandler handler, Collection<String> instrumentId) {
        impl = quick;
        client = handler;
    }

    @Override
    public void listen(Event<?> event) {
        switch (event.type()) {
            case SnapshotUpdate -> client.handle((Snapshot) event.event(), impl);
            case InstrumentStateUpdate -> impl.update((InstrumentState) event.event());
            case OrderStateUpdate -> impl.update((OrderState) event.event());
            case TradeUpdate -> impl.update((Trade) event.event());
            default -> throw new UnsupportedOperationException("Unsupported event type: " + event.type() + ".");
        }
    }
}
