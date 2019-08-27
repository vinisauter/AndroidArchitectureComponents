package com.vas.architecture.github.repository.datasource.retrofit.objects;

import java.util.List;

public class Result {
    public Boolean incomplete_results;
    public List<RepositoryRest> items;
    public Integer total_count;
}
