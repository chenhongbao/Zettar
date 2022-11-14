package io.zettar.core.event;

public class EventRouter {
    private EventRouter(){
    }

    public static EventRouter createRouter() {
        return new EventRouter();
    }

    public void publish(Event<?> event) {
    }

    public void listenByType(EventListener listener, EventType... types) {

    }

    public void listenByOrigin(EventListener listener, EventOriginType... origins) {

    }

    public void listen(EventListener listener) {

    }
}
