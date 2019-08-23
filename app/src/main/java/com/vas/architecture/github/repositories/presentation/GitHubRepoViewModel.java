package com.vas.architecture.github.repositories.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.State;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.vas.architecture.github.repositories.business.GitHubRepoModel;
import com.vas.architecture.github.repositories.business.Listing;
import com.vas.architecture.github.repositories.objects.Repo;

@SuppressWarnings("WeakerAccess")
public class GitHubRepoViewModel extends ViewModel {
    private static final int PAGE_SIZE = 10;
    private final GitHubRepoModel mainModel = GitHubRepoModel.getInstance();

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
    public final LiveData<State> refreshState = new MutableLiveData<>();

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
        // TODO: onQueryRetryRequest
    }

    public void refresh() {
//        repoResult.value?.refresh?.invoke()
    }
}
