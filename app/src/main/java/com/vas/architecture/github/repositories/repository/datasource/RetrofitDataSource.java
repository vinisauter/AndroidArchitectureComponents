package com.vas.architecture.github.repositories.repository.datasource;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vas.architecture.components.exception.RetryException;
import com.vas.architecture.github.repositories.repository.datasource.retrofit.GitHubService;
import com.vas.architecture.github.repositories.repository.datasource.retrofit.objects.RepositoryRest;
import com.vas.architecture.github.repositories.repository.datasource.retrofit.objects.Result;

import java.net.HttpRetryException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitDataSource extends PageKeyedDataSource<Integer, RepositoryRest> {
    private static final String BASE_URL = "https://api.github.com/";
    private final GitHubService service;
    private String query = "";
    private RestDataSourceCallback restDataSourceCallback;

    public interface RestDataSourceCallback {
        void onErrorHappened(RetryException error);
    }

    public RetrofitDataSource() {
        service = create(HttpUrl.parse(BASE_URL));
    }

    private GitHubService create(HttpUrl httpUrl) {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor(message -> Log.d("API", message));
        logger.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logger)
                .build();
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(GitHubService.class);
    }

    public DataSource.Factory<Integer, RepositoryRest> getRepositoriesRestFactory(String query, RestDataSourceCallback restDataSourceCallback) {
        this.query = query;
        this.restDataSourceCallback = restDataSourceCallback;
        return new DataSource.Factory<Integer, RepositoryRest>() {

            @NonNull
            @Override
            public DataSource<Integer, RepositoryRest> create() {
                return RetrofitDataSource.this;
            }
        };
    }

    // region PageKeyedDataSource
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, RepositoryRest> callback) {
        Integer per_page = params.requestedLoadSize;
        Integer page = 0;
        service.searchRepositories(query, per_page, page).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                if (response.body() != null) {
                    List<RepositoryRest> items = response.body().items;
                    callback.onResult(items, page - 1, page + 1);
                } else if (!response.isSuccessful()) {
                    Throwable throwable = new HttpRetryException(response.message(), response.code());
                    RetryException error = new RetryException(throwable, () -> loadInitial(params, callback));
                    if (restDataSourceCallback != null)
                        restDataSourceCallback.onErrorHappened(error);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable throwable) {
                RetryException error = new RetryException(throwable, () -> loadInitial(params, callback));
                if (restDataSourceCallback != null)
                    restDataSourceCallback.onErrorHappened(error);
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, RepositoryRest> callback) {
        // ignored, since we only ever append to our initial load
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, RepositoryRest> callback) {
        Integer per_page = params.requestedLoadSize;
        Integer page = params.key;
        service.searchRepositories(query, per_page, page).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                if (response.body() != null) {
                    List<RepositoryRest> items = response.body().items;
                    callback.onResult(items, page + 1);
                } else if (!response.isSuccessful()) {
                    Throwable throwable = new HttpRetryException(response.message(), response.code());
                    RetryException error = new RetryException(throwable, () -> loadAfter(params, callback));
                    if (restDataSourceCallback != null)
                        restDataSourceCallback.onErrorHappened(error);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable throwable) {
                RetryException error = new RetryException(throwable, () -> loadAfter(params, callback));
                if (restDataSourceCallback != null)
                    restDataSourceCallback.onErrorHappened(error);
            }
        });
    }
    // endregion PageKeyedDataSource
}