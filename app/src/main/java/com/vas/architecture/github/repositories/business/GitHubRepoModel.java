package com.vas.architecture.github.repositories.business;

import androidx.lifecycle.State;

import com.vas.architecture.components.exception.RetryException;
import com.vas.architecture.github.repositories.objects.Repo;
import com.vas.architecture.github.repositories.presentation.IGitHubData;
import com.vas.architecture.github.repositories.presentation.Listing;
import com.vas.architecture.github.repositories.repository.GitHubRepoRepository;

public class GitHubRepoModel implements IGitHubData {
    // region Singleton Model INSTANCE
    private static GitHubRepoModel instance_;

    public static GitHubRepoModel getInstance() {
        if (instance_ == null) {
            instance_ = createInstance();
        }
        return instance_;
    }

    // FIXME: Quem deveria criar a instancia da Model? Ela mesma?
    public static GitHubRepoModel createInstance() {
        instance_ = new GitHubRepoModel();
        return instance_;
    }

    // FIXME: Quem e como limpar a instancia da Model? Ela mesma?
    public static void clearInstance() {
        if (instance_ != null) {
            instance_ = null;
        }
    }
    // endregion

    private GitHubRepoRepository mainRepository = new GitHubRepoRepository();

    private final Listing<Repo> repoListing = new Listing<>();
    private boolean hasInternet = true;

    private GitHubRepoModel() {

    }

    public Listing<Repo> searchListRepos(String query, int pageSize) {
        mainRepository.searchRepositories(query, pageSize, hasInternet,
                repoListing.queryState,
                repoListing.pagedList,
                repoListing.onError);
        return repoListing;
    }

    public void retryErrorQuery() {
        State value = repoListing.queryState.getValue();
        if (value != null) {
            if (value == State.ERROR) {
                Throwable error = value.getThrowable();
                if (error instanceof RetryException) {
                    repoListing.queryState.postValue(State.LOADING);
                    ((RetryException) error).retry();
                } else {
                    error.printStackTrace();
                }
            }
        }
    }

    public void getRepoUrl(Long id) {
        mainRepository.getRepoUrl(id, repoListing.onUrlResult, repoListing.onError);
    }
}
