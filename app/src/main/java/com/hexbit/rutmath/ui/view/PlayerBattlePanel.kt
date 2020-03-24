package com.hexbit.rutmath.ui.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.hexbit.rutmath.R
import com.hexbit.rutmath.util.gone
import com.hexbit.rutmath.util.invisible
import com.hexbit.rutmath.util.visible
import kotlinx.android.synthetic.main.battle_player_view.view.*

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

    private var bonusToAdd = 0

    var canAnswer = true

    var totalPoints = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.battle_player_view, this, true)
        arrayOf(answer1, answer2, answer3, answer4).forEach {
            it.setOnClickListener { view ->
                if (!canAnswer) {
                    return@setOnClickListener
                }
                canAnswer = false
                lastSelectedButton = it
                listener?.onAnswerSelected((view as Button).text.toString().toInt())
            }
        }

        progressBar.apply {
            progress = 0
        }
        score.gone()
    }

    fun onCorrectAnswerSelected() {
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

    fun onIncorrectAnswerSelected() {
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

    fun setAnswers(answers: IntArray) {
        require(answers.size == 4)
        val answersButtons = arrayOf(answer1, answer2, answer3, answer4)
        answersButtons.forEachIndexed { index, button ->
            button.text = answers[index].toString()
            lastSelectedButton?.background =
                ColorDrawable(ContextCompat.getColor(context, R.color.colorAccent))
        }
        canAnswer = true
    }

    private fun showBonus(bonusValue: Int) {
        bonus.text = context.getString(R.string.bonus, bonusValue)
        bonus.visible()
        Handler().postDelayed({
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
}
