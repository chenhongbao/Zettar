package io.zettar.core.quick;

import io.zettar.SourceException;

public class IneligibleOrderException extends SourceException {
    public IneligibleOrderException(Integer code, String message) {
        super(code, message);
    }
}
