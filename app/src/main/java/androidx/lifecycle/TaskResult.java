package androidx.lifecycle;

import java.util.Objects;

/**
 * Container to ease passing around a tuple of two objects. This object provides a sensible
 * implementation of equals(), returning true if equals() is true on each of the contained
 * objects.
 */
public class TaskResult<F> {
    public final F value;
    public final Throwable error;

    /**
     * Constructor for a TaskResult.
     *
     * @param value the result object in the TaskResult
     * @param error the error object in the TaskResult
     */
    public TaskResult(F value, Throwable error) {
        this.value = value;
        this.error = error;
    }

    /**
     * Checks the two objects for equality by delegating to their respective
     * {@link Objects#equals} methods.
     *
     * @param o the {@link TaskResult} to which this one is to be checked for equality
     * @return true if the underlying objects of the TaskResult are both considered
     * equal
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TaskResult)) {
            return false;
        }
        TaskResult<?> p = (TaskResult<?>) o;
        return Objects.equals(p.value, value) && Objects.equals(p.error, error);
    }

    /**
     * Compute a hash code using the hash codes of the underlying objects
     *
     * @return a hashcode of the TaskResult
     */
    @Override
    public int hashCode() {
        return (value == null ? 0 : value.hashCode()) ^ (error == null ? 0 : error.hashCode());
    }

    @Override
    public String toString() {
        return "TaskResult{" + String.valueOf(value) + " " + String.valueOf(error) + "}";
    }

    /**
     * Convenience method for creating an appropriately typed TaskResult.
     *
     * @param a the result object in the TaskResult
     * @param b the error object in the TaskResult
     * @return a TaskResult that is templatized with the types of a and b
     */
    public static <A> TaskResult<A> create(A a, Throwable b) {
        return new TaskResult<>(a, b);
    }

    public State getState() {
        if (error != null) {
            return State.ERROR.setThrowable(error);
        } else if (value != null) {
            return State.SUCCEEDED;
        } else {
            return State.LOADING;
        }
    }
}
