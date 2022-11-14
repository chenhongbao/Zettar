package io.zettar.core.quick;

import io.zettar.core.event.EventListener;
import io.zettar.core.event.EventOriginType;
import io.zettar.core.event.EventType;

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
