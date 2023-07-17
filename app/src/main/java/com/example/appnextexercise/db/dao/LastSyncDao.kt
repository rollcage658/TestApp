package com.example.appnextexercise.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appnextexercise.db.entity.LastSyncEntity

@Dao
interface LastSyncDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLastSyncMs(lastSyncMs: LastSyncEntity)

    @Query("DELETE FROM last_sync")
    suspend fun deleteLastSyncMs();

    @Query("SELECT * FROM last_sync")
    suspend fun getLastSyncMs(): Long
}