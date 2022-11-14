package io.zettar;

import java.time.ZonedDateTime;
import java.util.Date;

public record OrderState(
        String orderId,
        ZonedDateTime whenUpdated,
        Date tradingDay,
        long tradeQuantity,
        int submitStateCode,
        int stateCode,
        String message) {
}
