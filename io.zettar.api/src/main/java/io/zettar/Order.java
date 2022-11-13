package io.zettar;

public record OrderUpdate(String orderId,
                          String groupId,
                          String instrumentId,
                          String instrumentName,
                          String exchangeId,
                          double price,
                          long quantity,
                          boolean buyOrSell,
                          boolean openOrClose,
                          boolean todayOrNot) {
}
