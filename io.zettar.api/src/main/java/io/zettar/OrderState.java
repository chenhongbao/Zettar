package io.zettar;

import java.time.ZonedDateTime;
import java.util.Date;

public record OrderUpdate(
        String orderId,
        String groupId,
        ZonedDateTime timeStamp,
        Date tradingDay,
        long tradeQuantity,
        int submitStateCode,
        int stateCode,
        String message) {
}
