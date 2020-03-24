package com.hexbit.rutmath.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Score")
data class Score(
    @ColumnInfo(name = "nick")
    val nick: String,
    @ColumnInfo(name = "score")
    val score: Int,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) : Serializable