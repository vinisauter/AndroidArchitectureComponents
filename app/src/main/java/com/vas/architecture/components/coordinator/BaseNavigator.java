package com.vas.architecture.components.coordinator;

public abstract class BaseNavigator {
    protected final NavigationOwner owner;

    public BaseNavigator(NavigationOwner owner) {
        this.owner = owner;
    }
}
