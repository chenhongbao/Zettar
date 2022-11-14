package io.zettar.core.quick;

import io.zettar.*;
import io.zettar.core.event.EventRouter;
import io.zettar.core.logging.LoggingUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

class QuickImpl implements Quick {
    private final ConcurrentHashMap<String, OrderContext> contexts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Boolean> states = new ConcurrentHashMap<>();
    private final EventRouter evt;
    private final Logger logger;

    QuickImpl(EventRouter router) {
        evt = router;
        logger = LoggingUtils.createLogger(this, router);
    }

    @Override
    public void buyOpen(String exchangeId, String instrumentId, long quantity) {

    }

    @Override
    public void buyClose(String exchangeId, String instrumentId, long quantity) {

    }

    @Override
    public void sellOpen(String exchangeId, String instrumentId, long quantity) {

    }

    @Override
    public void sellClose(String exchangeId, String instrumentId, long quantity) {

    }

    @Override
    public Logger getLogger(String loggerName) {
        return logger;
    }

    public void update(OrderState state) {
        OrderContext context = contexts.get(state.orderId());
        if (context  != null) {
            context.append(state);
            if (!state.good() || state.leaveQuantity() == 0) {
                context.wakeUp();
            }
        }
    }

    public void update(InstrumentState state) {
        states.put(state.instrumentId(), state.trading());
    }

    public void update(Trade trade) {
        OrderContext context = contexts.get(trade.orderId());
        if (context  != null) {
            context.append(trade);
        }
    }

    private boolean trading(String instrumentId) {
        return false;
    }
}
