package com.vas.architecture;

import androidx.lifecycle.TaskLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.TimeUnit;

import rx.Observable;

public class RootViewModel extends ViewModel {

    public TaskLiveData<Boolean> someTask() {
        return TaskLiveData.createFor(Observable.just(true).delay(5, TimeUnit.SECONDS));
    }
}
