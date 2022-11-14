package io.zettar.core.event;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class EventRouter {
    private final ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();
    private final EventSubscriber subscriber = new EventSubscriber();
    private final HashMap<EventType, EventExecutor> executors = new HashMap<>() {
        {
            put(EventType.OrderInsertion, new EventExecutor(subscriber));
            put(EventType.Subscription, new EventExecutor(subscriber));
            put(EventType.OrderStateUpdate, new EventExecutor(subscriber));
            put(EventType.TradeUpdate, new EventExecutor(subscriber));
            put(EventType.InstrumentStateUpdate, new EventExecutor(subscriber));
            put(EventType.SnapshotUpdate, new EventExecutor(subscriber));
            put(EventType.ClientLogging, new EventExecutor(subscriber));
            put(EventType.SystemLogging, new EventExecutor(subscriber));
            put(EventType.SystemPanic, new EventExecutor(subscriber));
        }
    };

    private EventRouter() {
        executors.values().forEach(pool::submit);
    }

    public static EventRouter createRouter() {
        return new EventRouter();
    }

    /**
     * Publish event to event listeners.
     *
     * <p>Events of the same type are guaranteed to execute in sequence, but events of different types or from
     * different origins can be executed in parallel.
     *
     * @param event The event to be published to its subscribing listeners.
     */
    public void publish(Event<?> event) {
        if (logIfNull(event, "Null event.") != null && logIfNull(event.type(), "Null event type.") != null) {
            executors.get(event.type()).append(event);
        }
    }

    public void listenByType(EventListener listener, EventType... types) {
        subscriber.subscribeTypeListener(listener, types);
    }

    public void listenByOrigin(EventListener listener, EventSourceType... origins) {
        subscriber.subscribeOriginListener(listener, origins);
    }

    public void listen(EventListener listener) {
        subscriber.subscribeListener(listener);
    }

    private <T> T logIfNull(T object, String message) {
        if (object == null) {
            systemLog(message);
        }
        return object;
    }

    private void systemLog(String message) {
        publish(new Event<>(EventUtils.getEventId(), EventUtils.getInstanceId(this), "Default Event Router Group", EventType.SystemLogging, EventSourceType.InternalSystem, ZonedDateTime.now(), new LogRecord(Level.WARNING, message)));
    }

    private class EventSubscriber {
        private final ConcurrentHashMap<EventType, ConcurrentSkipListSet<EventListener>> eventTypes = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<EventSourceType, ConcurrentSkipListSet<EventListener>> originTypes = new ConcurrentHashMap<>();
        private final ConcurrentSkipListSet<EventListener> all = new ConcurrentSkipListSet<>(new EventListenerComparator());

        public void publish(Event<?> event) {
            if (logIfNull(event, "Null event.") != null) {
                EventType type = event.type();
                if (logIfNull(event.type(), "Null event type.") != null && eventTypes.containsKey(type)) {
                    eventTypes.get(type).parallelStream().forEach(listener -> tryCatch(() -> listener.listen(event)));
                }

                EventSourceType originType = event.sourceType();
                if (logIfNull(originType, "Null origin type.") != null && originTypes.containsKey(originType)) {
                    originTypes.get(originType).parallelStream().forEach(listener -> tryCatch(() -> listener.listen(event)));
                }

                all.parallelStream().forEach(listener -> tryCatch(() -> listener.listen(event)));
            }
        }

        private void tryCatch(Runnable callable) {
            try {
                callable.run();
            } catch (Exception exception) {
                systemLog("Calling client code throws exception: " + exception.getMessage());
            }
        }

        public void subscribeListener(EventListener listener) {
            all.add(listener);
        }

        public void subscribeOriginListener(EventListener listener, EventSourceType[] origins) {
            for (EventSourceType origin : origins) {
                subscribeSingleOrigin(listener, origin);
            }
        }

        private void subscribeSingleOrigin(EventListener listener, EventSourceType origin) {
            subscribeGenericType(originTypes, listener, origin);
        }

        public void subscribeTypeListener(EventListener listener, EventType[] types) {
            for (EventType type : types) {
                subscribeSingleType(listener, type);
            }
        }

        private void subscribeSingleType(EventListener listener, EventType type) {
            subscribeGenericType(eventTypes, listener, type);
        }

        private <T> void subscribeGenericType(Map<T,ConcurrentSkipListSet<EventListener>> map, EventListener listener, T type) {
            ConcurrentSkipListSet<EventListener> listeners = map.computeIfAbsent(type, typeKey -> new ConcurrentSkipListSet<>(new EventListenerComparator()));
            listeners.add(listener);
        }

        private class EventListenerComparator implements Comparator<EventListener> {
            @Override
            public int compare(EventListener l0, EventListener l1) {
                return l0.hashCode() - l1.hashCode();
            }
        }
    }

    private class EventExecutor implements Runnable {
        private final BlockingQueue<Event<?>> snapEvents = new LinkedBlockingQueue<>();
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition condition = lock.newCondition();
        private final EventSubscriber sub;
        private final Thread thread;

        public EventExecutor(EventSubscriber subscriber) {
            sub = subscriber;
            thread = new Thread(this);
            thread.start();
        }

        @Override
        public void run() {
            handleEvent();
            lock.lock();
            try {
                condition.signal();
            } finally {
                lock.unlock();
            }
        }

        public boolean exit() throws InterruptedException {
            if (thread.isAlive()) {
                thread.interrupt();
            }
            lock.lock();
            try {
                return condition.await(1, TimeUnit.SECONDS);
            } finally {
                lock.unlock();
            }
        }

        public void append(Event<?> event) {
            if (!snapEvents.offer(event)) {
                throw new QueueOverflowException("Queue overflows and takes no more event: " + event + ".");
            }
        }

        private void handleEvent() {
            boolean interrupted =  false;
            while (!interrupted || !snapEvents.isEmpty()) {
                Event<?> event = null;
                try {
                    event = snapEvents.poll(1, TimeUnit.SECONDS);
                    if (event != null) {
                        subscriber.publish(event);
                    }
                } catch (InterruptedException exception) {
                    interrupted = true;
                }
            }
        }
    }
}
