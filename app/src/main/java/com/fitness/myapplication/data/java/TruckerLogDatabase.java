package com.fitness.myapplication.data.java;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TruckerLog.class}, version = 2, exportSchema = false)
public abstract class TruckerLogDatabase extends RoomDatabase {
    public abstract TruckerLogDao truckerLogDao();

    private static volatile TruckerLogDatabase INSTANCE;

    public static TruckerLogDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TruckerLogDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TruckerLogDatabase.class, "trucker_log_database_java")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
