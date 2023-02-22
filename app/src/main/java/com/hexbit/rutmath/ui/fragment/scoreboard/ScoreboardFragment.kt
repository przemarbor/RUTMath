package com.hexbit.rutmath.ui.fragment.scoreboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.hexbit.rutmath.R
import com.hexbit.rutmath.data.AppDatabase
import com.hexbit.rutmath.databinding.FragmentScoreboardBinding
import com.hexbit.rutmath.util.base.BaseFragment
import com.hexbit.rutmath.util.gone
import com.hexbit.rutmath.util.visible
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class ScoreboardFragment : BaseFragment() {

    override val layout = R.layout.fragment_scoreboard
    private var _binding: FragmentScoreboardBinding? = null
    private val binding get() = _binding!!
    private val database: AppDatabase by inject()

    private val disposables = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScoreboardBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        disposables.add(database.scoreDao().getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { scoreList ->
                val sortedList = scoreList.sortedBy { it.score }.reversed()
                binding.recyclerView.adapter = ScoreboardAdapter(sortedList)
                if (sortedList.isNotEmpty()) {
                    binding.emptyListInstruction.gone()
                    binding.scoreBoardInfo.visible()
                }
            })
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
        _binding = null
    }
}