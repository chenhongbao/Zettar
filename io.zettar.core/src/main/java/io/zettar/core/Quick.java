package io.zettar.core;

public interface Quick {
    void buyOpen(String exchangeId, String instrumentId, long quantity);
    void buyClose(String exchangeId, String instrumentId, long quantity);
    void sellOpen(String exchangeId, String instrumentId, long quantity);
    void sellClose(String exchangeId, String instrumentId, long quantity);
}
