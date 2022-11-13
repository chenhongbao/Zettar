package io.zettar;

public record Subscription(String[] instrumentId) {
    public static final int OPTION_UNSUBSCRIBE = 0x10000000;
}
