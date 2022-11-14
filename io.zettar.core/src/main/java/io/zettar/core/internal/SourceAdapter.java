package io.zettar.core.internal;

import io.zettar.Order;
import io.zettar.Source;
import io.zettar.Subscription;
import io.zettar.core.Event;
import io.zettar.core.EventListener;

import java.util.Objects;

public class SourceAdapter implements EventListener {
    private final Source src;

    public SourceAdapter(Source source) {
        Objects.requireNonNull(source);
        src = source;
    }

    @Override
    public void listen(Event<?> event) {
        Objects.requireNonNull(event);
        switch (event.type()) {
            case OrderInsertion -> src.insert((Order) event.event());
            case Subscription -> src.subscribe((Subscription) event.event());
            default -> throw new UnsupportedOperationException("Unsupported event type: " + event.type() + ".");
        }
    }
}
