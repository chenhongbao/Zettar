package io.zettar.core.internal;

import io.zettar.*;
import io.zettar.core.*;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class QuickEngineImpl implements QuickEngine {
    private final EventRouter router = new EventRouter();

    private String sourceClass;
    private QuickImpl quick;
    private HashSet<String> subscribeId;

    public QuickEngineImpl() {
        quick = new QuickImpl(router);
        subscribeId = new HashSet<>();
    }

    @Override
    public QuickEngine handledBy(QuickHandler handler, String... instrumentId) {
        List<String> id = Arrays.asList(instrumentId);
        router.listen(new QuickHandlerWrapper(quick, handler, id));
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
    public QuickEngine listenByOrigin(EventListener listener, EventOriginType... origins) {
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
        final String sourceId = EventUtils.getSourceId(source);

        source.handledBy((Trade trade) -> quickPublish(trade, EventType.TradeUpdate, sourceId));
        source.handledBy((InstrumentState state) -> quickPublish(state, EventType.InstrumentStateUpdate, sourceId));
        source.handledBy((Snapshot snap) -> quickPublish(snap, EventType.SnapshotUpdate, sourceId));
        source.handledBy(((OrderState state) -> quickPublish(state, EventType.OrderStateUpdate, sourceId)));
        source.subscribe(new Subscription(subscribeId.toArray(new String[0])), 0);

        router.listenByType(new SourceAdapter(source), EventType.OrderInsertion, EventType.Subscription);

        subscribeId = null;
        quick = null;
    }

    private <T> void quickPublish(T object, EventType type, String sourceId) {
        router.publish(new Event(EventUtils.getEventId(), type, object, new EventOrigin(EventOriginType.SourceInput, sourceId), ZonedDateTime.now()));
    }
}
