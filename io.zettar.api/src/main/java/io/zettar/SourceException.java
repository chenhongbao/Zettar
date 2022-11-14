package io.zettar;

public class SourceException extends RuntimeException{
    private final String message;
    private final int code;

    public SourceException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
