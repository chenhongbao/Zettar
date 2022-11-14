package io.zettar.core.quick;

import io.zettar.InstrumentState;
import io.zettar.OrderState;
import io.zettar.Snapshot;
import io.zettar.Trade;
import io.zettar.core.event.Event;
import io.zettar.core.event.EventListener;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

class QuickHandlerWrapper implements EventListener {
    private final ConcurrentHashMap<String, Boolean> gotStates = new ConcurrentHashMap<>();
    private final QuickHandler client;
    private final QuickImpl impl;

    QuickHandlerWrapper(QuickImpl quick, QuickHandler handler, Collection<String> instrumentId) {
        impl = quick;
        client = handler;
    }

    @Override
    public void listen(Event<?> event) {
        switch (event.type()) {
            case SnapshotUpdate -> {
                Snapshot snap = (Snapshot) event.event();
                client.handle(snap, impl);
                if (!gotStates.getOrDefault(snap.instrumentId(), false)) {
                    impl.update(getFakeInstrumentState(snap));
                    gotStates.put(snap.instrumentId(), true);
                }
            }
            case InstrumentStateUpdate -> impl.update((InstrumentState) event.event());
            case OrderStateUpdate -> impl.update((OrderState) event.event());
            case TradeUpdate -> impl.update((Trade) event.event());
            default -> throw new UnsupportedOperationException("Unsupported event type: " + event.type() + ".");
        }
    }

    private InstrumentState getFakeInstrumentState(Snapshot snap) {
        long seconds = Duration.between(snap.whenUpdated(), ZonedDateTime.now()).getSeconds();
        /* Lag 5 seconds at most. */
        return new InstrumentState(snap.exchangeId(), snap.instrumentId(), snap.instrumentName(), ZonedDateTime.now(), snap.tradingDay(), 0, seconds <= 5);
    }
}
