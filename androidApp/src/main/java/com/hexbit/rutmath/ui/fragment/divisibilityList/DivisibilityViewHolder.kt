package com.hexbit.rutmath.ui.fragment.divisibilityList

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hexbit.rutmath.R
import com.hexbit.rutmath.data.model.ExerciseType
import com.hexbit.rutmath.databinding.DivisibilityExerciseItemBinding


class DivisibilityViewHolder(
    private val binding: DivisibilityExerciseItemBinding,
    private val clickCallback: (exerciseType: ExerciseType) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {

    private val difficultyList = listOf(
        binding.root.resources.getString(R.string.divisibility_difficulty_1),
        binding.root.resources.getString(R.string.divisibility_difficulty_2),
        binding.root.resources.getString(R.string.divisibility_difficulty_3),
        binding.root.resources.getString(R.string.divisibility_difficulty_4)
    )

    fun bind(exerciseType: ExerciseType) = with(binding){
        title.text = when (exerciseType.difficulty) {
            1,2,3 -> difficultyList[0]
            4,5,6 -> difficultyList[1]
            9,8,7 -> difficultyList[2]
            10 -> difficultyList[3]
            else -> null
        }.plus(" ").plus(((exerciseType.difficulty-1) % 3)+1)
        if (exerciseType.difficulty == 10)
            title.text = title.text.dropLast(2)

        /**
         *  Set a listener on unlocked exercise tile and change its color
         */
        if (exerciseType.isUnlocked) {
            root.setOnClickListener {
                clickCallback.invoke(exerciseType)
            }
            root.background = ContextCompat.getDrawable(root.context, R.drawable.bg_in_tile_exercise)
        }

        val stars = arrayOf(star1, star2, star3, star4, star5)

        // Reset all stars to the default white icon
        stars.forEach { star ->
            star.setImageResource(R.drawable.ic_star)
        }

        for (i in 1..exerciseType.rate) {
            stars[i - 1].setImageResource(R.drawable.ic_star_yellow_24dp)
        }
    }
}