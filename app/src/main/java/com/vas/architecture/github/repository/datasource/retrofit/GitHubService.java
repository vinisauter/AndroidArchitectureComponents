package com.vas.architecture.github.repository.datasource.retrofit;

import com.vas.architecture.github.repository.datasource.retrofit.objects.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GitHubService {
    @GET("/search/repositories?")
    Call<Result> searchRepositories(
            @Query("q") String query,
            @Query("per_page") Integer per_page,
            @Query("page") Integer page
    );
}
