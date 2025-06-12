package com.hexbit.rutmath.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Player")
data class Player(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "nick")
    val nick: String
) : Serializable