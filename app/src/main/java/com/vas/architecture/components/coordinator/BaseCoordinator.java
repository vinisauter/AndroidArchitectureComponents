package com.vas.architecture.components.coordinator;

import androidx.lifecycle.ViewModelProvider;

public abstract class BaseCoordinator {
    protected final NavigationOwner owner;

    public BaseCoordinator(NavigationOwner owner) {
        this.owner = owner;
    }

    public ViewModelProvider getViewModelProvider() {
        return new ViewModelProvider(owner);
    }

    public abstract void start();

    public abstract void finish();
}
