package io.zettar;

import java.time.ZonedDateTime;
import java.util.Date;

public record Snapshot(
        String exchangeId,
        String instrumentId,
        String instrumentName,
        ZonedDateTime whenUpdated,
        Date tradingDay,
        double askPrice,
        long askVolume,
        double bidPrice,
        long bidVolume,
        double averagePrice,
        double lastPrice,
        long volume,
        long openInterest,
        double settlementPrice,
        double closePrice,
        double preSettlementPrice,
        double preClosePrice,
        double preOpenInterest) {
}
