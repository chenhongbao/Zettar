package io.zettar.core.quick;

import io.zettar.Trade;

public class PositionManager {
    public Position[] requirePosition(String exchangeId, String instrumentId, long quantity, boolean buyOrSell, boolean openOrClose) {
        return new Position[0];
    }

    public PositionContext getOrder(String orderId) {
        return null;
    }
}
