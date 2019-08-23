package com.vas.architecture.github.repositories.repository.datasource.room;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.vas.architecture.AppApplication;
import com.vas.architecture.BuildConfig;
import com.vas.architecture.github.repositories.repository.datasource.room.objects.RepositoryDB;

@Database(version = BuildConfig.VERSION_CODE, exportSchema = false,
        entities = {
                RepositoryDB.class
        })
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract RepositoryDao repositoryDao();

    public static AppDatabase getInstance() {
        synchronized (AppDatabase.class) {
            if (INSTANCE == null || !INSTANCE.isOpen()) {
                //FIXME: Application Context Dependency is a problem? AppApplication.getInstance()
                INSTANCE = Room.databaseBuilder(AppApplication.getInstance(), AppDatabase.class, "app-database")
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return INSTANCE;
        }
    }

    public static void destroyInstance() {
        synchronized (AppDatabase.class) {
            INSTANCE = null;
        }
    }
}