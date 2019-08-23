package com.vas.architecture.github.repositories.repository.datasource.room.objects;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class RepositoryDB {
    @PrimaryKey(autoGenerate = true)
    public Long id;
    public Date createdAt;
    public Date updatedAt;

    public String name;
    public String full_name;
    public String html_url;
    public String description;
    public String created_at;
    public String updated_at;
    public String pushed_at;
    public String language;
    public Double score;
    public Integer size;
    public Integer stargazers_count;
    public Integer watchers_count;
    public Integer forks_count;
    public String archive_url;
    public Boolean archived;
    public String assignees_url;
    public String blobs_url;
    public String branches_url;
    public String clone_url;
    public String collaborators_url;
    public String comments_url;
    public String commits_url;
    public String compare_url;
    public String contents_url;
    public String contributors_url;
    public String default_branch;
    public String deployments_url;
    public Boolean disabled;
    public String downloads_url;
    public String events_url;
    public Boolean fork;
    public Integer forks;
    public String forks_url;
    public String git_commits_url;
    public String git_refs_url;
    public String git_tags_url;
    public String git_url;
    public Boolean has_downloads;
    public Boolean has_issues;
    public Boolean has_pages;
    public Boolean has_projects;
    public Boolean has_wiki;
    public String homepage;
    public String hooks_url;
    public String issue_comment_url;
    public String issue_events_url;
    public String issues_url;
    public String keys_url;
    public String labels_url;
    public String languages_url;
    public String merges_url;
    public String milestones_url;
    public String mirror_url;
    public String node_id;
    public String notifications_url;
    public Integer open_issues;
    public Integer open_issues_count;
    public String pulls_url;
    public String releases_url;
    public String ssh_url;
    public String stargazers_url;
    public String statuses_url;
    public String subscribers_url;
    public String subscription_url;
    public String svn_url;
    public String tags_url;
    public String teams_url;
    public String trees_url;
    public String url;
    public Integer watchers;
    @Embedded(prefix = "owner_")
    public Owner owner;
    @Embedded(prefix = "license_")
    public License license;

    public RepositoryDB() {
        createdAt = new Date();
        updatedAt = new Date();
    }
}
