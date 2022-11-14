package io.zettar.core.internal;

import io.zettar.core.Event;
import io.zettar.core.EventListener;
import io.zettar.core.Quick;
import io.zettar.core.QuickHandler;

import java.util.Collection;

public class QuickHandlerWrapper implements EventListener {
    private final QuickHandler client;
    private final Quick impl;

    public QuickHandlerWrapper(Quick quick, QuickHandler handler, Collection<String> instrumentId) {
        impl = quick;
        client = handler;
    }

    @Override
    public void listen(Event event) {

    }
}
