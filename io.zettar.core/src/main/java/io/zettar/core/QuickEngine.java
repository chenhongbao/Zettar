package io.zettar.core;

import io.zettar.core.internal.QuickEngineImpl;

public interface QuickEngine {
    static QuickEngine createEngine() {
        return new QuickEngineImpl();
    }

    QuickEngine handledBy(QuickHandler handler, String... instrumentId);
    QuickEngine setSource(String sourceClassPath);
    QuickEngine addListener(EventListener... listeners);
    void run();
}
