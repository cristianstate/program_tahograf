package com.fitness.myapplication.data.java;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TruckerLogDao {
    @Insert
    void insert(TruckerLog log);

    @Update
    void update(TruckerLog log);

    @Delete
    void delete(TruckerLog log);

    @Query("SELECT * FROM trucker_logs_java ORDER BY date DESC")
    LiveData<List<TruckerLog>> getAllLogs();

    @Query("SELECT * FROM trucker_logs_java WHERE id = :id")
    TruckerLog getLogById(int id);

    @Query("SELECT * FROM trucker_logs_java WHERE date LIKE '%' || :date || '%' ORDER BY date DESC")
    LiveData<List<TruckerLog>> searchLogsByDate(String date);
}
