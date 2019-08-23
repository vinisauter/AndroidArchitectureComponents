package com.vas.architecture.components.exception;

public class RetryException extends Exception {
    private final Runnable retry;

    public RetryException(Throwable error, Runnable retry) {
        super(error);
        this.retry = retry;
    }

    public Runnable getRetry() {
        return retry;
    }

    public void retry() {
        retry.run();
    }
}
