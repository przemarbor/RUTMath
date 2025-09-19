package com.octbit.rutmath.ui.fragment.game.battle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.octbit.rutmath.R
import com.octbit.rutmath.data.AppDatabase
import com.octbit.rutmath.databinding.FragmentPlayersNamesBinding
import com.octbit.rutmath.util.base.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class PlayersNamesFragment : BaseFragment() {

    override val layout = R.layout.fragment_players_names
    private var _binding: FragmentPlayersNamesBinding? = null
    private val binding get() = _binding!!
    private val database: AppDatabase by inject()

    private val disposables = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayersNamesBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding){
        super.onViewCreated(view, savedInstanceState)
        disposables.add(
            database.settingsDao().getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map { it[0] }
                .subscribe { settings ->
                    player1NicknameEditText.setText(settings.lastNickname1)
                    player2NicknameEditText.setText(settings.lastNickname2)
                }
        )

        startBattleButton.setOnClickListener {
            val player1nick = player1NicknameEditText.text.toString()
            val player2nick = player2NicknameEditText.text.toString()

            if (player1nick.isEmpty()) {
                player1NicknameInputLayout.error = getString(R.string.nick_empty)
                return@setOnClickListener
            }
            if (player2nick.isEmpty()) {
                player2NicknameInputLayout.error = getString(R.string.nick_empty)
                return@setOnClickListener
            }
            player1NicknameInputLayout.error = null
            player2NicknameInputLayout.error = null

            disposables.add(
                database.settingsDao().getAll()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .map { it[0] }
                    .flatMapCompletable {
                        database.settingsDao().update(
                            it.copy(
                                lastNickname1 = player1nick,
                                lastNickname2 = player2nick
                            )
                        ).observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                    }
                    .subscribe {
                        findNavController().navigate(
                            PlayersNamesFragmentDirections.actionPlayersNamesFragmentToBattleGameFragment(
                                player1nick,
                                player2nick
                            )
                        )
                    }
            )
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
        _binding = null
    }
}