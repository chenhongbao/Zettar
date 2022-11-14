package io.zettar.core.internal;

import io.zettar.Source;
import io.zettar.core.Event;
import io.zettar.core.EventListener;

public class SourceAdapter implements EventListener {
    private final Source src;

    public SourceAdapter(Source source) {
        src = source;
    }

    @Override
    public void listen(Event event) {

    }
}
