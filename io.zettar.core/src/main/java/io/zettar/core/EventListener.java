package io.zettar.core;

@FunctionalInterface
public interface EventListener {
    void listen(Event<?> event);
}
