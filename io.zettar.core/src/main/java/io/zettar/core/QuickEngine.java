package io.zettar.core;

import io.zettar.core.internal.QuickEngineImpl;

public interface QuickEngine {
    static QuickEngine createEngine() {
        return new QuickEngineImpl();
    }

    QuickEngine handledBy(QuickHandler handler, String... instrumentId);
    QuickEngine setSource(String sourceClassPath);
    QuickEngine listenByType(EventListener listener, EventType... types);
    QuickEngine listenByOrigin(EventListener listener, EventOriginType... origins);
    QuickEngine listen(EventListener listener);
    void run();
}
