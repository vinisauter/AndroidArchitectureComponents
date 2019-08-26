package androidx.lifecycle;

import com.vas.architecture.components.Utils;

/**
 * The current state of a unit of work.
 */
public enum State {
    LOADING,
    SUCCEEDED,
    ERROR;

    public boolean isLoading() {
        return this == LOADING;
    }

    public boolean isError() {
        return this == ERROR;
    }

    public boolean isSucceeded() {
        return this == SUCCEEDED;
    }

    public boolean isFinished() {
        return (this == SUCCEEDED || this == ERROR);
    }

    private Throwable error;

    public String getMessage() {
        return Utils.getTextFor(error);
    }

    public Throwable getThrowable() {
        return error;
    }

    public State setThrowable(Throwable error) {
        this.error = error;
        return this;
    }
}
