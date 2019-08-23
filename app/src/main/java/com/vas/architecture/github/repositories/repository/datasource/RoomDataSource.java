package com.vas.architecture.github.repositories.repository.datasource;

import androidx.paging.DataSource;
import androidx.paging.PagedList;
import androidx.paging.RxPagedListBuilder;

import com.vas.architecture.github.repositories.objects.Repo;
import com.vas.architecture.github.repositories.repository.datasource.room.AppDatabase;
import com.vas.architecture.github.repositories.repository.datasource.room.RepositoryDao;
import com.vas.architecture.github.repositories.repository.datasource.room.objects.RepositoryDB;

import java.text.MessageFormat;

import io.reactivex.Observable;

public class RoomDataSource {

    private final RepositoryDao repositoryDao = AppDatabase.getInstance().repositoryDao();

    public void save(RepositoryDB input) {
        repositoryDao.save(input);
    }

    public DataSource.Factory<Integer, RepositoryDB> getRepositoriesDBFactory(String query) {
        return repositoryDao.getRepositoriesPaging(query);
    }

    public Observable<PagedList<Repo>> getRepositories(String query, int pageSize) {
        String dbLikeQuery = MessageFormat.format("%{0}%", query);
        return new RxPagedListBuilder<>(getRepositoriesDBFactory(dbLikeQuery).map(input -> {
            Repo repo = new Repo();
            repo.id = input.id;
            repo.description = input.description;
            repo.score = input.score;
            repo.imageUrl = input.owner.avatar_url;
            return repo;
        }), pageSize).buildObservable();
    }

    public Observable<PagedList<RepositoryDB>> getRepositoriesDB(String query, int pageSize) {
        return new RxPagedListBuilder<>(getRepositoriesDBFactory(MessageFormat.format("%{0}%", query)), pageSize).buildObservable();
    }
}
