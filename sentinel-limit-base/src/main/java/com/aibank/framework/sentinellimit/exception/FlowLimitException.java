package com.aibank.framework.sentinellimit.exception;

public class FlowLimitException extends RuntimeException {
    public FlowLimitException(String message) {
        super(message);
    }
}
