package io.zettar;

import java.time.ZonedDateTime;
import java.util.Date;

public record Trade(
        String orderId,
        String tradeId,
        String exchangeId,
        String instrumentId,
        String instrumentName,
        ZonedDateTime whenUpdated,
        Date tradingDay,
        double price,
        long volume,
        boolean buyOrSell,
        boolean openOrClose,
        Offset todayOrNot) {
}
