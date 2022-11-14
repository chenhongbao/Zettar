package io.zettar.core.internal;

import io.zettar.*;
import io.zettar.core.EventListener;
import io.zettar.core.Quick;
import io.zettar.core.QuickBuilder;
import io.zettar.core.QuickHandler;

import java.util.Arrays;
import java.util.HashSet;

public class QuickBuilderImpl implements QuickBuilder {
    private QuickHandler quickHandler;
    private String sourceClass;
    private final HashSet<String> subscribeId = new HashSet<>();
    private final HashSet<EventListener> eventListeners = new HashSet<>();

    @Override
    public QuickBuilder handledBy(QuickHandler handler, String... instrumentId) {
        quickHandler = handler;
        subscribeId.addAll(Arrays.asList(instrumentId));
        return this;
    }

    @Override
    public QuickBuilder setSource(String sourceClassPath) {
        sourceClass = sourceClassPath;
        return this;
    }

    @Override
    public QuickBuilder addListener(EventListener... listeners) {
        eventListeners.addAll(Arrays.asList(listeners));
        return this;
    }

    @Override
    public Quick build() {
        eventListeners.add(new QuickHandlerWrapper(quickHandler));

        Source source = SourceFactory.lookupSource(sourceClass);
        QuickImpl impl = new QuickImpl(source, eventListeners);

        source.handledBy((TradeHandler) impl);
        source.handledBy((OrderStateHandler) impl);
        source.handledBy((SnapshotHandler) impl);
        source.handledBy((InstrumentStateHandler) impl);

        source.subscribe(new Subscription(subscribeId.toArray(new String[0])), 0);
        return impl;
    }


}
