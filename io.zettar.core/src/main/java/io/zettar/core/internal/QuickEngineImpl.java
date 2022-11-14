package io.zettar.core.internal;

import io.zettar.*;
import io.zettar.core.EventListener;
import io.zettar.core.QuickEngine;
import io.zettar.core.QuickHandler;

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
        router.addListener(new QuickHandlerWrapper(quick, handler, id));
        subscribeId.addAll(id);
        return this;
    }

    @Override
    public QuickEngine setSource(String sourceClassPath) {
        sourceClass = sourceClassPath;
        return this;
    }

    @Override
    public QuickEngine addListener(EventListener... listeners) {
        for(EventListener listener : listeners) {
            router.addListener(listener);
        }
        return this;
    }

    @Override
    public void run() {
        Source source = SourceFactory.lookupSource(sourceClass);

        source.handledBy((TradeHandler) router);
        source.handledBy((InstrumentStateHandler) router);
        source.handledBy((SnapshotHandler) router);
        source.handledBy(((TradeHandler) router));
        source.subscribe(new Subscription(subscribeId.toArray(new String[0])), 0);

        router.setSource(source);
        subscribeId = null;
        quick = null;
    }
}
