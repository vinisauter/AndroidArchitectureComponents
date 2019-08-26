package com.vas.architecture.github.repositories.business;

import com.vas.architecture.github.repositories.objects.Repo;
import com.vas.architecture.github.repositories.repository.GitHubRepoRepositoryV2;

public class GitHubRepoModel {
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

    private GitHubRepoRepositoryV2 mainRepository = new GitHubRepoRepositoryV2();

    private GitHubRepoModel() {

    }

    public Listing<Repo> searchListRepos(String query, int pageSize) {
        return mainRepository.searchRepositories(query, pageSize);
    }

    public void retryErrorQuery() {
        mainRepository.retryErrorQuery();
    }
}
