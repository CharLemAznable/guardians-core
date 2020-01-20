package com.github.charlemaznable.guardians.exception;

public final class GuardianException extends RuntimeException {

    private static final long serialVersionUID = -6438996019472050220L;

    public GuardianException() {
        super();
    }

    public GuardianException(String message) {
        super(message);
    }

    public GuardianException(String message, Throwable cause) {
        super(message, cause);
    }

    public GuardianException(Throwable cause) {
        super(cause);
    }
}
