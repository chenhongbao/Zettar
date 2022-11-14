package io.zettar.core.internal;

import io.zettar.core.Event;
import io.zettar.core.EventListener;
import io.zettar.core.QuickHandler;

public class QuickHandlerWrapper implements EventListener {
    private final QuickHandler client;

    public QuickHandlerWrapper(QuickHandler handler) {
        client = handler;
    }

    @Override
    public void listen(Event event) {

    }
}
