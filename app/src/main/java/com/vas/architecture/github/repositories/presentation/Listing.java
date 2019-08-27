package com.vas.architecture.github.repositories.presentation;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SingleLiveEvent;
import androidx.lifecycle.State;
import androidx.paging.PagedList;

public class Listing<T> {
    public final MutableLiveData<PagedList<T>> pagedList = new MutableLiveData<>();
    public final MutableLiveData<State> queryState = new MutableLiveData<>();
    public final SingleLiveEvent<Throwable> onError = new SingleLiveEvent<>();
    public final SingleLiveEvent<String> onUrlResult = new SingleLiveEvent<>();
}