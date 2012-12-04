package no.spp.sdk.exception;

public class SPPClientException extends Exception {
    public SPPClientException(String message) {
        super(message);
    }

    public SPPClientException(Throwable throwable) {
        super(throwable);
    }

    public SPPClientException(String message, Throwable throwable) {
        super(message, throwable);
    }

}