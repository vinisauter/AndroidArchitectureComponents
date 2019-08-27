package com.vas.architecture.github.repositories.presentation;

import com.vas.architecture.github.repositories.objects.Repo;

public interface IGitHubData {
    Listing<Repo> searchListRepos(String query, int pageSize);

    void retryErrorQuery();

    void getRepoUrl(Long id);
}
