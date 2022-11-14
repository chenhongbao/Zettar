package io.zettar;

public record Subscription(
        String clientId,
        String[] instrumentId,
        int options) {
    public static final int OPTION_UNSUBSCRIBE = 0x10000000;
}
