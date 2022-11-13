package io.zettar;

import java.time.ZonedDateTime;
import java.util.Date;

public record InstrumentState(
        String exchangeId,
        String instrumentId,
        String instrumentName,
        ZonedDateTime enterTime,
        Date tradingDay,
        int stateCode) {
}
