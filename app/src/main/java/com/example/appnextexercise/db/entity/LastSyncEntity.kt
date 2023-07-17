package com.example.appnextexercise.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "last_sync")
data class LastSyncEntity( @PrimaryKey
                           @ColumnInfo(defaultValue = "0") val timeStampMs : Long) {
}