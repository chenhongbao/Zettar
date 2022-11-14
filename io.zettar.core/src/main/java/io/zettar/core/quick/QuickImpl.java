package io.zettar.core.quick;

import io.zettar.*;
import io.zettar.core.event.*;
import io.zettar.core.logging.LoggingUtils;

import java.time.ZonedDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

class QuickImpl implements Quick {
    private final ConcurrentHashMap<String, Boolean> trading = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Snapshot> snapshots = new ConcurrentHashMap<>();
    private final OrderManager orderManager = new OrderManager();
    private final PositionManager positionManager = new PositionManager();
    private final static AtomicLong orderId = new AtomicLong(0);
    private final EventRouter evt;
    private final String cliId;
    private final Logger logger;

    QuickImpl(String clientId, EventRouter router) {
        evt = router;
        cliId = clientId;
        logger = LoggingUtils.createLogger(this, router);
    }

    private static String getOrderId() {
        return Long.toString(orderId.incrementAndGet());
    }

    @Override
    public void buyOpen(String exchangeId, String instrumentId, long quantity) {
        eligible(instrumentId, quantity);
        executeOrders(exchangeId, instrumentId, quantity, true, true);
    }

    @Override
    public void buyClose(String exchangeId, String instrumentId, long quantity) {
        eligible(instrumentId, quantity);
        executeOrders(exchangeId, instrumentId, quantity, true, false);
    }

    @Override
    public void sellOpen(String exchangeId, String instrumentId, long quantity) {
        eligible(instrumentId, quantity);
        executeOrders(exchangeId, instrumentId, quantity, false, true);
    }

    @Override
    public void sellClose(String exchangeId, String instrumentId, long quantity) {
        eligible(instrumentId, quantity);
        executeOrders(exchangeId, instrumentId, quantity, false, false);
    }

    @Override
    public Logger getLogger(String loggerName) {
        return logger;
    }

    public void update(OrderState state) {
        OrderContext context = orderManager.getOrder(state.orderId());
        if (context != null) {
            context.update(state);
        }
    }

    public void update(InstrumentState state) {
        trading.put(state.instrumentId(), state.trading());
    }

    public void update(Trade trade) {
        PositionContext context = positionManager.getOrder(trade.orderId());
        if (context != null) {
            context.update(trade);
        }
    }

    public void update(Snapshot snapshot) {
        snapshots.put(snapshot.instrumentId(), snapshot);
    }

    private void eligible(String instrumentId, long quantity) {
        if (!trading.getOrDefault(instrumentId, false) || quantity <= 0) {
            throw new IneligibleOrderException(null, "Ineligible order.");
        }
        if (snapshots.get(instrumentId) == null) {
            throw new IneligibleOrderException(null, "Not ready for order: " + instrumentId + ".");
        }
    }

    private void publish(Order order, OrderGroup group) {
        evt.publish(new Event<>(EventUtils.getEventId(), cliId, group.getGroupId(), EventType.OrderInsertion, EventSourceType.CodeClient, ZonedDateTime.now(), order));
    }

    private Order[] computeOrders(String exchangeId, String instrumentId, long quantity, boolean buyOrSell, boolean openOrClose) {
        Position[] available = positionManager.requirePosition(exchangeId, instrumentId, quantity, buyOrSell, openOrClose);
        if (available.length == 0) {
            return new Order[0];
        } else {
            Order[] orders = new Order[available.length];
            for (int index = 0; index < available.length; ++index) {
                double price = snapshots.get(instrumentId).upperLimitPrice();
                long volume = available[index].getVolume();
                Offset offset = available[index].getOpenTradingDay().compareTo(snapshots.get(instrumentId).tradingDay()) == 0 ? Offset.Today : Offset.Yesterday;
                orders[index] = new Order(getOrderId(), exchangeId, instrumentId, "", price, volume, buyOrSell, openOrClose, offset, 0);
            }
            return orders;
        }
    }

    private void executeOrders(String exchangeId, String instrumentId, long quantity, boolean buyOrSell, boolean openOrClose) {
        Order[] orders = computeOrders(exchangeId, instrumentId, quantity, buyOrSell, openOrClose);
        if (orders.length == 0) {
            throw new IneligibleOrderException(0, "Insufficient " + (buyOrSell == openOrClose ? "long" : "short") + " position to " + (openOrClose ? "open" : "close") + ": " + instrumentId + ".");
        } else {
            OrderGroup group = orderManager.createOrderGroup(orders);
            for (Order order : orders) {
                publish(order, group);
            }
            group.joinOrders();
        }
    }
}
