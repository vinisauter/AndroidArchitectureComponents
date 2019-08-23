package androidx.lifecycle;

import androidx.annotation.NonNull;

import io.reactivex.disposables.Disposable;
import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

public class TaskLiveData<T> extends SingleLiveEvent<TaskResult<T>> {

    public static <T> TaskLiveData<T> createFor(io.reactivex.Observable<T> observable) {
        return new TaskLiveData<T>() {
            Disposable subscription = observable
                    .subscribeOn(io.reactivex.schedulers.Schedulers.newThread())
                    .subscribe(this::setSuccess, this::setError);

            @Override
            protected void onInactive() {
                subscription.dispose();
            }
        };
    }

    public static <T> TaskLiveData<T> createFor(Observable<T> observable) {
        return new TaskLiveData<T>() {
            Subscription subscription = observable
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(this::setSuccess, this::setError);

            @Override
            protected void onInactive() {
                subscription.unsubscribe();
            }
        };
    }

    public static <T> TaskLiveData<T> error(Throwable throwable) {
        return new TaskLiveData<>(new TaskResult<>(null, throwable));
    }

    public static <T> TaskLiveData<T> success(T t) {
        return new TaskLiveData<>(new TaskResult<>(t, null));
    }

    public void setError(Throwable throwable) {
        postValue(new TaskResult<>(null, throwable));
    }

    public void setSuccess(T t) {
        postValue(new TaskResult<>(t, null));
    }

    public TaskLiveData(TaskResult<T> pair) {
        postValue(pair);
    }

    public TaskLiveData() {
    }

    public TaskLiveData<T> observeState(@NonNull LifecycleOwner owner, @NonNull Observer<State> observer) {
        state().observe(owner, observer);
        return this;
    }

    public LiveData<State> state() {
        final MutableLiveData<State> statusLiveData = new MutableLiveData<>();
        statusLiveData.postValue(State.LOADING);
        return Transformations.switchMap(this, input -> {
            if (input != null) {
                if (input.error != null) {
                    statusLiveData.postValue(State.ERROR.setError(input.error));
                } else {
                    statusLiveData.postValue(State.SUCCEEDED);
                }
            }
            return statusLiveData;
        });
    }

    public TaskLiveData<T> observeValue(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
        value().observe(owner, observer);
        return this;
    }

    public LiveData<T> value() {
        final MutableLiveData<T> tLiveData = new MutableLiveData<>();
        return Transformations.switchMap(this, input -> {
            if (input != null && input.value != null) {
                tLiveData.postValue(input.value);
            }
            return tLiveData;
        });
    }

    public TaskLiveData<T> observeError(@NonNull LifecycleOwner owner, @NonNull Observer<Throwable> observer) {
        error().observe(owner, observer);
        return this;
    }

    public LiveData<Throwable> error() {
        final MutableLiveData<Throwable> throwableLiveData = new MutableLiveData<>();
        return Transformations.switchMap(this, input -> {
            if (input != null && input.error != null) {
                throwableLiveData.postValue(input.error);
            }
            return throwableLiveData;
        });
    }

    @Override
    protected void onActive() {
        super.onActive();
    }

    @Override
    protected void onInactive() {
        super.onInactive();
    }

    public interface TaskObserver<T> {
        void onFailed(Throwable error);

        void onSucceeded(T value);
    }

    public interface TaskStatusObserver<T> extends TaskObserver<T> {
        void onStateChanged(State state);
    }

    public void observe(@NonNull LifecycleOwner owner, @NonNull TaskStatusObserver<T> observer) {
        observer.onStateChanged(State.LOADING);
        observe(owner, new TaskObserver<T>() {
            @Override
            public void onFailed(Throwable error) {
                observer.onFailed(error);
                observer.onStateChanged(State.ERROR.setError(error));
            }

            @Override
            public void onSucceeded(T value) {
                observer.onSucceeded(value);
                observer.onStateChanged(State.SUCCEEDED);
            }
        });
    }

    public void observe(@NonNull LifecycleOwner owner, @NonNull TaskObserver<T> observer) {
        super.observe(owner, tTaskResult -> {
            if (tTaskResult.error != null) {
                observer.onFailed(tTaskResult.error);
            }
            if (tTaskResult.value != null) {
                observer.onSucceeded(tTaskResult.value);
            }
        });
    }

    public void observeForever(@NonNull LifecycleOwner owner, @NonNull TaskObserver<T> observer) {
        super.observeForever(tTaskResult -> {
            if (tTaskResult.error != null) {
                observer.onFailed(tTaskResult.error);
            }
            if (tTaskResult.value != null) {
                observer.onSucceeded(tTaskResult.value);
            }
        });
    }
}