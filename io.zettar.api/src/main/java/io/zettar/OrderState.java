package io.zettar;

import java.time.ZonedDateTime;
import java.util.Date;

public record OrderState(
        String orderId,
        ZonedDateTime whenUpdated,
        Date tradingDay,
        long tradeQuantity,
        long leaveQuantity,
        int submitStateCode,
        int stateCode,
        boolean good,
        String message) {
}
