package com.vas.architecture.github.repositories.repository.datasource;

import androidx.paging.DataSource;

import com.vas.architecture.github.repositories.repository.datasource.room.AppDatabase;
import com.vas.architecture.github.repositories.repository.datasource.room.RepositoryDao;
import com.vas.architecture.github.repositories.repository.datasource.room.objects.RepositoryDB;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class RoomDataSource {

    private final RepositoryDao repositoryDao = AppDatabase.getInstance().repositoryDao();

    public void save(RepositoryDB input) {
//        repositoryDao.save(input);
        Observable.fromCallable(() -> repositoryDao.save(input)).subscribeOn(Schedulers.io()).subscribe();
    }

    public DataSource.Factory<Integer, RepositoryDB> getRepositoriesDBFactory(String query) {
        return repositoryDao.getRepositoriesPaging(query);
    }

    public Observable<RepositoryDB> getRepositoryById(Long id) {
        return Observable.fromCallable(() -> repositoryDao.getRepositoryById(id)).subscribeOn(Schedulers.io());
    }
}
