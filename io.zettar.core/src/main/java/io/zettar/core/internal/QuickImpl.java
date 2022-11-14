package io.zettar.core.internal;

import io.zettar.*;
import io.zettar.core.Quick;

import java.util.logging.Logger;

class QuickImpl implements Quick {
    private final EventRouter evt;

    QuickImpl(EventRouter router) {
        evt = router;
    }

    @Override
    public void buyOpen(String exchangeId, String instrumentId, long quantity) {

    }

    @Override
    public void buyClose(String exchangeId, String instrumentId, long quantity) {

    }

    @Override
    public void sellOpen(String exchangeId, String instrumentId, long quantity) {

    }

    @Override
    public void sellClose(String exchangeId, String instrumentId, long quantity) {

    }

    @Override
    public Logger getLogger(String loggerName) {
        return null;
    }
}
