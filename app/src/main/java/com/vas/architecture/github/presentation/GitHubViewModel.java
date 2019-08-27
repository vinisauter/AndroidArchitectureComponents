package com.vas.architecture.github.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SingleLiveEvent;
import androidx.lifecycle.State;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.vas.architecture.components.Utils;
import com.vas.architecture.github.business.GitHubRepoModel;
import com.vas.architecture.github.presentation.objects.Listing;
import com.vas.architecture.github.presentation.objects.Repo;

@SuppressWarnings("WeakerAccess")
public class GitHubViewModel extends ViewModel {
    private static final int PAGE_SIZE = 10;
    private final IGitHubDataModel mainModel = GitHubRepoModel.getInstance();

    private final MutableLiveData<String> query = new MutableLiveData<>();

    private final LiveData<Listing<Repo>> repoResult = Transformations.map(query, input ->
            mainModel.searchListRepos(input, PAGE_SIZE)
    );

    public final LiveData<PagedList<Repo>> posts = Transformations.switchMap(repoResult, input ->
            input.pagedList
    );
    public final LiveData<State> queryState = Transformations.switchMap(repoResult, input ->
            input.queryState
    );

    public final LiveData<String> errorToShow = Transformations.switchMap(repoResult, input ->
            Transformations.map(input.onError, Utils::getTextFor)
    );

    public final SingleLiveEvent<String> navigateToBrowser = SingleLiveEvent.switchMap(repoResult, input ->
            input.onUrlResult
    );

    public void setQuery(String query) {
        if (query.trim().equalsIgnoreCase(this.query.getValue())) {
            return;
        }
        this.query.setValue(query.trim());
    }

    public String getCurrentQueryValue() {
        return query.getValue();
    }

    public void onQueryRetryRequest() {
        mainModel.retryErrorQuery();
    }

    public void onRepoSelected(Repo item) {
        mainModel.getRepoUrl(item.id);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        GitHubRepoModel.clearInstance();
    }
}
