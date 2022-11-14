package io.zettar.core.event;

import java.util.concurrent.atomic.AtomicLong;

public class EventUtils {
    private static final AtomicLong eventId = new AtomicLong(0);

    public static String getEventId() {
        return Long.toString(eventId.incrementAndGet());
    }

    public static String getSourceId(Object object) {
        if (object == null) {
            return "{null}";
        }
        return object.getClass().getName() + "@" + object.hashCode();
    }
}
