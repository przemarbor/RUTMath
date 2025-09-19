package com.octbit.rutmath.ui.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.octbit.rutmath.R
import com.octbit.rutmath.databinding.BattlePlayerViewBinding
import com.octbit.rutmath.util.gone
import com.octbit.rutmath.util.invisible
import com.octbit.rutmath.util.visible

class PlayerBattlePanel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    interface PanelListener {
        fun onAnswerSelected(value: Int)
    }

    companion object {
        private const val BONUS_VISIBILITY_TIME_MS = 900L
    }

    private var listener: PanelListener? = null

    private var lastSelectedButton: Button? = null

    private var _binding: BattlePlayerViewBinding? = null
    private val binding get() = _binding!!

    private var bonusToAdd = 0

    var canAnswer = true

    var totalPoints = 0

    init {
        _binding = BattlePlayerViewBinding.inflate(LayoutInflater.from(context), this, true)
        arrayOf(binding.answer1, binding.answer2, binding.answer3, binding.answer4).forEach {
            it.setOnClickListener { view ->
                if (!canAnswer) {
                    return@setOnClickListener
                }
                canAnswer = false
                lastSelectedButton = it
                listener?.onAnswerSelected((view as Button).text.toString().toInt())
            }
        }

        binding.progressBar.apply {
            progress = 0
        }
        binding.score.gone()
    }

    fun onCorrectAnswerSelected() = with(binding){
        progressBar.progress++
        totalPoints = progressBar.progress + bonusToAdd
        score.text = totalPoints.toString()
        score.visible()
        lastSelectedButton?.background =
            ColorDrawable(ContextCompat.getColor(context, R.color.green))
        if (bonusToAdd > 0) {
            showBonus(bonusToAdd)
        }
        bonusToAdd++
    }

    fun onIncorrectAnswerSelected() = with(binding){
        bonusToAdd = 0
        progressBar.progress--
        totalPoints--
        if (progressBar.progress <= 0) {
            progressBar.progress = 0
            score.gone()
        } else {
            score.text = totalPoints.toString()
            score.visible()
        }
        lastSelectedButton?.background = ColorDrawable(ContextCompat.getColor(context, R.color.red))
    }

    fun setAnswers(answers: IntArray) = with(binding) {
        require(answers.size == 4)
        val answersButtons = arrayOf(answer1, answer2, answer3, answer4)
        answersButtons.forEachIndexed { index, button ->
            button.text = answers[index].toString()
            lastSelectedButton?.background =
                ColorDrawable(ContextCompat.getColor(context, R.color.accent))
        }
        canAnswer = true
    }

    private fun showBonus(bonusValue: Int) = with(binding) {
        bonus.text = context.getString(R.string.battle_view_bonus, bonusValue)
        bonus.visible()
        Handler(Looper.getMainLooper()).postDelayed({
            bonus.invisible()
        }, BONUS_VISIBILITY_TIME_MS)
    }

    fun getResult(): Int {
        return totalPoints
    }

    fun clearBonus() {
        bonusToAdd = 0
    }

    fun setListener(listener: PanelListener) {
        this.listener = listener
    }

    fun setEquation(text: String){
        binding.equation.text = text
    }

    fun setProgressBar(progress:Int, max:Int){
        binding.progressBar.progress = progress
        binding.progressBar.max = max
    }

    fun getProgressBar(): ProgressBar {
        return binding.progressBar
    }
}
