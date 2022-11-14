package io.zettar.core;

import io.zettar.core.internal.QuickBuilderImpl;

public interface QuickBuilder {
    static QuickBuilder createBuilder() {
        return new QuickBuilderImpl();
    }

    QuickBuilder handledBy(QuickHandler handler, String... instrumentId);
    QuickBuilder setSource(String sourceClassPath);
    QuickBuilder addListener(EventListener... listeners);
    Quick build();
}
