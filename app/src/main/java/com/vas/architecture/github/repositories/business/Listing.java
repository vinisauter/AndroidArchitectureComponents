package com.vas.architecture.github.repositories.business;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.State;
import androidx.paging.PagedList;

public class Listing<T> {
    public LiveData<PagedList<T>> pagedList;
    public MutableLiveData<State> queryState;
    public Runnable retry;
}