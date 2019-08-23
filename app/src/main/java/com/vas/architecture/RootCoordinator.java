package com.vas.architecture;

import androidx.lifecycle.TaskLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.vas.architecture.components.Utils;
import com.vas.architecture.components.coordinator.BaseCoordinator;
import com.vas.architecture.components.coordinator.NavigationOwner;

public class RootCoordinator extends BaseCoordinator {
    private final RootNavigator navigator;
    private final RootViewModel viewModel;

    public RootCoordinator(NavigationOwner owner) {
        super(owner);
        this.navigator = new RootNavigator(owner);
        ViewModelProvider viewModelProvider = new ViewModelProvider(owner);
        viewModel = viewModelProvider.get(RootViewModel.class);
    }

    @Override
    public void start() {
        viewModel.someTask().observe(owner, new TaskLiveData.TaskObserver<Boolean>() {
            @Override
            public void onFailed(Throwable error) {
                navigator.showError(Utils.getTextFor(error));
            }

            @Override
            public void onSucceeded(Boolean value) {
                navigator.showGitHubRepoSearch();
            }
        });
    }

    @Override
    public void finish() {
        //
    }
}
