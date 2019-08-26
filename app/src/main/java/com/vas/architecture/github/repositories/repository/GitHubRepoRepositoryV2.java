package com.vas.architecture.github.repositories.repository;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.State;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;

import com.vas.architecture.components.exception.RetryException;
import com.vas.architecture.github.repositories.business.Listing;
import com.vas.architecture.github.repositories.objects.Repo;
import com.vas.architecture.github.repositories.repository.datasource.RetrofitDataSource;
import com.vas.architecture.github.repositories.repository.datasource.RoomDataSource;
import com.vas.architecture.github.repositories.repository.datasource.retrofit.objects.RepositoryRest;
import com.vas.architecture.github.repositories.repository.datasource.room.objects.License;
import com.vas.architecture.github.repositories.repository.datasource.room.objects.Owner;
import com.vas.architecture.github.repositories.repository.datasource.room.objects.RepositoryDB;

// Neste caso quem implementa IGitHubData é o repository e não o DataSource
public class GitHubRepoRepositoryV2 {
    private RoomDataSource roomDataSource = new RoomDataSource();

    private RetrofitDataSource retrofitDataSource = new RetrofitDataSource();

    private boolean hasInternet = true;
    private final Listing<Repo> repoListing;

    public GitHubRepoRepositoryV2() {
        repoListing = new Listing<>();
        repoListing.queryState = new MutableLiveData<>();
    }

    public Listing<Repo> searchRepositories(String query, int pageSize) {
        repoListing.queryState.postValue(State.LOADING);
        DataSource.Factory<Integer, RepositoryDB> dsFactory;
        if (hasInternet) {
            dsFactory = retrofitDataSource.getRepositoriesRestFactory(query, error -> {
                repoListing.queryState.postValue(State.ERROR.setThrowable(error));
                repoListing.retry = error.getRetry();
            }).map(GitHubRepoRepositoryV2.this::mapAndSaveToDB).mapByPage(input -> {
                if (input.isEmpty())
                    repoListing.queryState.postValue(State.SUCCEEDED);
                return input;
            });
        } else {
            dsFactory = roomDataSource.getRepositoriesDBFactory(query);
        }
        repoListing.pagedList = new LivePagedListBuilder<>(dsFactory.map(input -> {
            Repo repo = new Repo();
            repo.id = input.id;
            repo.description = input.description;
            repo.score = input.score;
            repo.imageUrl = input.owner.avatar_url;
            return repo;
        }), pageSize).build();
        return repoListing;
    }

    private RepositoryDB mapAndSaveToDB(RepositoryRest input) {
        RepositoryDB repositoryDB = new RepositoryDB();

        // region Copia os atributos de RepositoryRest para RepositoryDB
        repositoryDB.id = input.id;
        repositoryDB.name = input.name;
        repositoryDB.full_name = input.full_name;
        repositoryDB.html_url = input.html_url;
        repositoryDB.description = input.description;
        repositoryDB.created_at = input.created_at;
        repositoryDB.updated_at = input.updated_at;
        repositoryDB.pushed_at = input.pushed_at;
        repositoryDB.language = input.language;
        repositoryDB.score = input.score;
        repositoryDB.size = input.size;
        repositoryDB.stargazers_count = input.stargazers_count;
        repositoryDB.watchers_count = input.watchers_count;
        repositoryDB.forks_count = input.forks_count;
        repositoryDB.archive_url = input.archive_url;
        repositoryDB.archived = input.archived;
        repositoryDB.assignees_url = input.assignees_url;
        repositoryDB.blobs_url = input.blobs_url;
        repositoryDB.branches_url = input.branches_url;
        repositoryDB.clone_url = input.clone_url;
        repositoryDB.collaborators_url = input.collaborators_url;
        repositoryDB.comments_url = input.comments_url;
        repositoryDB.commits_url = input.commits_url;
        repositoryDB.compare_url = input.compare_url;
        repositoryDB.contents_url = input.contents_url;
        repositoryDB.contributors_url = input.contributors_url;
        repositoryDB.default_branch = input.default_branch;
        repositoryDB.deployments_url = input.deployments_url;
        repositoryDB.disabled = input.disabled;
        repositoryDB.downloads_url = input.downloads_url;
        repositoryDB.events_url = input.events_url;
        repositoryDB.fork = input.fork;
        repositoryDB.forks = input.forks;
        repositoryDB.forks_url = input.forks_url;
        repositoryDB.git_commits_url = input.git_commits_url;
        repositoryDB.git_refs_url = input.git_refs_url;
        repositoryDB.git_tags_url = input.git_tags_url;
        repositoryDB.git_url = input.git_url;
        repositoryDB.has_downloads = input.has_downloads;
        repositoryDB.has_issues = input.has_issues;
        repositoryDB.has_pages = input.has_pages;
        repositoryDB.has_projects = input.has_projects;
        repositoryDB.has_wiki = input.has_wiki;
        repositoryDB.homepage = input.homepage;
        repositoryDB.hooks_url = input.hooks_url;
        repositoryDB.issue_comment_url = input.issue_comment_url;
        repositoryDB.issue_events_url = input.issue_events_url;
        repositoryDB.issues_url = input.issues_url;
        repositoryDB.keys_url = input.keys_url;
        repositoryDB.labels_url = input.labels_url;
        repositoryDB.languages_url = input.languages_url;
        repositoryDB.merges_url = input.merges_url;
        repositoryDB.milestones_url = input.milestones_url;
        repositoryDB.mirror_url = input.mirror_url;
        repositoryDB.node_id = input.node_id;
        repositoryDB.notifications_url = input.notifications_url;
        repositoryDB.open_issues = input.open_issues;
        repositoryDB.open_issues_count = input.open_issues_count;
        repositoryDB.pulls_url = input.pulls_url;
        repositoryDB.releases_url = input.releases_url;
        repositoryDB.ssh_url = input.ssh_url;
        repositoryDB.stargazers_url = input.stargazers_url;
        repositoryDB.statuses_url = input.statuses_url;
        repositoryDB.subscribers_url = input.subscribers_url;
        repositoryDB.subscription_url = input.subscription_url;
        repositoryDB.svn_url = input.svn_url;
        repositoryDB.tags_url = input.tags_url;
        repositoryDB.teams_url = input.teams_url;
        repositoryDB.trees_url = input.trees_url;
        repositoryDB.url = input.url;
        repositoryDB.watchers = input.watchers;
        repositoryDB.owner = new Owner();
        repositoryDB.owner.login = input.owner.login;
        repositoryDB.owner.avatar_url = input.owner.avatar_url;
        repositoryDB.owner.events_url = input.owner.events_url;
        repositoryDB.owner.followers_url = input.owner.followers_url;
        repositoryDB.owner.following_url = input.owner.following_url;
        repositoryDB.owner.gists_url = input.owner.gists_url;
        repositoryDB.owner.gravatar_id = input.owner.gravatar_id;
        repositoryDB.owner.html_url = input.owner.html_url;
        repositoryDB.owner.node_id = input.owner.node_id;
        repositoryDB.owner.organizations_url = input.owner.organizations_url;
        repositoryDB.owner.received_events_url = input.owner.received_events_url;
        repositoryDB.owner.repos_url = input.owner.repos_url;
        repositoryDB.owner.site_admin = input.owner.site_admin;
        repositoryDB.owner.starred_url = input.owner.starred_url;
        repositoryDB.owner.subscriptions_url = input.owner.subscriptions_url;
        repositoryDB.owner.type = input.owner.type;
        repositoryDB.owner.url = input.owner.url;
        repositoryDB.license = new License();
        if (input.license != null) {
            repositoryDB.license.key = input.license.key;
            repositoryDB.license.name = input.license.name;
            repositoryDB.license.node_id = input.license.node_id;
            repositoryDB.license.spdx_id = input.license.spdx_id;
            repositoryDB.license.url = input.license.url;
        }
        // endregion

        roomDataSource.save(repositoryDB);
        return repositoryDB;
    }

    public void retryErrorQuery() {
        if (repoListing.queryState != null) {
            State value = repoListing.queryState.getValue();
            if (value != null) {
                if (value == State.ERROR) {
                    Throwable error = value.getThrowable();
                    if (error instanceof RetryException) {
                        repoListing.queryState.postValue(State.LOADING);
                        ((RetryException) error).retry();
                    } else
                        error.printStackTrace();
                }
            }
        }
    }
}
