package io.zettar;

public record Order(
        String clientId,
        String orderId,
        String exchangeId,
        String instrumentId,
        String instrumentName,
        double price,
        long quantity,
        boolean buyOrSell,
        boolean openOrClose,
        boolean todayOrNot,
        int options) {

    public static final int OPTION_FAK = 0x00000001;
    public static final int OPTION_FOK = 0x00000002;
    public static final int OPTION_DELETE = 0x00000004;
}
