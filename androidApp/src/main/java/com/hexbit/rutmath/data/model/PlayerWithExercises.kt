package com.hexbit.rutmath.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class PlayerWithExercises(
    @Embedded val player: Player,
    @Relation(
        parentColumn = "nick",
        entityColumn = "userNick"
    )
    val scores: List<ExerciseType>
)