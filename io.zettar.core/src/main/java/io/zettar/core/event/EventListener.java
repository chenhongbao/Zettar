package io.zettar.core.event;

@FunctionalInterface
public interface EventListener {
    void listen(Event<?> event);
}
