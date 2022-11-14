package io.zettar.core.quick;

import io.zettar.*;
import io.zettar.core.event.*;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

class QuickEngineImpl implements QuickEngine {
    private final EventRouter router = EventRouter.createRouter();
    private String sourceClass;
    private HashSet<String> subscribeId;

    QuickEngineImpl() {
        subscribeId = new HashSet<>();
    }

    @Override
    public QuickEngine handledBy(String clientId, QuickHandler handler, String... instrumentId) {
        List<String> id = Arrays.asList(instrumentId);
        router.listenByOrigin(new QuickHandlerWrapper(new QuickImpl(clientId, router), handler, id), EventSourceType.SourceInput);
        subscribeId.addAll(id);
        return this;
    }

    @Override
    public QuickEngine setSource(String sourceClassPath) {
        sourceClass = sourceClassPath;
        return this;
    }

    @Override
    public QuickEngine listenByType(EventListener listener, EventType... types) {
        router.listenByType(listener, types);
        return this;
    }

    @Override
    public QuickEngine listenByOrigin(EventListener listener, EventSourceType... origins) {
        router.listenByOrigin(listener, origins);
        return this;
    }

    @Override
    public QuickEngine listen(EventListener listener) {
        router.listen(listener);
        return this;
    }

    @Override
    public void run() {
        final Source source = SourceFactory.lookupSource(sourceClass);
        final String originId = EventUtils.getInstanceId(source);

        source.handledBy((Trade trade) -> sourceInput(trade, EventType.TradeUpdate, originId));
        source.handledBy((InstrumentState state) -> sourceInput(state, EventType.InstrumentStateUpdate, originId));
        source.handledBy((Snapshot snap) -> sourceInput(snap, EventType.SnapshotUpdate, originId));
        source.handledBy(((OrderState state) -> sourceInput(state, EventType.OrderStateUpdate, originId)));
        source.subscribe(new Subscription("Default Quick Engine", subscribeId.toArray(new String[0]), 0));

        router.listenByType(new OrderInsertListener(source, router), EventType.OrderInsertion, EventType.Subscription);
        subscribeId = null;
    }

    private <T> void sourceInput(T object, EventType type, String originId) {
        router.publish(new Event<>(EventUtils.getEventId(), originId, "Default-Source-Input-Group", type, EventSourceType.SourceInput, ZonedDateTime.now(), object));
    }
}
