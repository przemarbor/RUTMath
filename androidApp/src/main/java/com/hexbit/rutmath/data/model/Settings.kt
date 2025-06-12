package com.hexbit.rutmath.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Settings")
data class Settings(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "maxNumberInBattleMode")
    var maxNumberInBattleMode: Int,
    @ColumnInfo(name = "language")
    var language: String,
    @ColumnInfo(name = "lastNickname1")
    var lastNickname1: String,
    @ColumnInfo(name = "lastNickname2")
    var lastNickname2: String
)