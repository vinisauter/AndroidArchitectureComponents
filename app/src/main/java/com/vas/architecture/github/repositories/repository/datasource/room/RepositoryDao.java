package com.vas.architecture.github.repositories.repository.datasource.room;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.vas.architecture.github.repositories.repository.datasource.room.objects.RepositoryDB;

import java.util.List;

@Dao
public interface RepositoryDao {

    // can't run in android main thread
    @Query("SELECT * FROM RepositoryDB")
    List<RepositoryDB> getRepositories();

    // can run in android main thread
    @Query("SELECT * FROM RepositoryDB")
    LiveData<List<RepositoryDB>> getRepositoriesLiveData();

    // can run in android main thread
    @Query("SELECT * FROM RepositoryDB WHERE name LIKE :query " +
            "OR full_name LIKE :query " +
            "OR description LIKE :query "
    )
    DataSource.Factory<Integer, RepositoryDB> getRepositoriesPaging(String query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long save(RepositoryDB item);

    @Delete
    void delete(RepositoryDB... item);

    @Query("SELECT * FROM RepositoryDB WHERE id = :id ")
    RepositoryDB getRepositoryById(Long id);
}
