package com.fitness.myapplication.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TruckerLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: TruckerLog)

    @Update
    suspend fun update(log: TruckerLog)

    @Delete
    suspend fun delete(log: TruckerLog)

    @Query("SELECT * FROM trucker_logs ORDER BY date DESC")
    fun getAllLogs(): Flow<List<TruckerLog>>

    @Query("SELECT * FROM trucker_logs WHERE id = :id")
    suspend fun getLogById(id: Int): TruckerLog?

    @Query("SELECT * FROM trucker_logs WHERE date LIKE '%' || :date || '%' ORDER BY date DESC")
    fun searchLogsByDate(date: String): Flow<List<TruckerLog>>
}
