package com.vas.architecture.github.presentation;

import com.vas.architecture.github.presentation.objects.Listing;
import com.vas.architecture.github.presentation.objects.Repo;

public interface IGitHubDataModel {
    Listing<Repo> searchListRepos(String query, int pageSize);

    void retryErrorQuery();

    void getRepoUrl(Long id);
}
