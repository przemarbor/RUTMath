package com.hexbit.rutmath.ui.fragment.choosePlayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hexbit.rutmath.data.AppDatabase
import com.hexbit.rutmath.data.model.Player
import com.hexbit.rutmath.util.base.DisposableViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ChoosePlayerViewModel(
    private val database: AppDatabase
) : DisposableViewModel() {

    enum class PlayerCreationEvent {
        SUCCESS,
        NICKNAME_EXISTS,
        NICKNAME_EMPTY
    }

    private val playersList = arrayListOf<Player>()

    private val refreshPlayersListEvent = MutableLiveData<Unit>()

    private val playerCreationEvent = MutableLiveData<PlayerCreationEvent>()

    fun getRefreshPlayersListEvent(): LiveData<Unit> = refreshPlayersListEvent

    fun getPlayersList(): List<Player> = playersList

    fun playerCreationEvent(): LiveData<PlayerCreationEvent> = playerCreationEvent

    fun loadPlayersList() {
        manageDisposable {
            database
                .userDao()
                .getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { list ->
                    playersList.apply {
                        clear()
                        addAll(list)
                    }
                    refreshPlayersListEvent.postValue(Unit)
                }
        }
    }

    fun createPlayer(nick: String) {
        manageDisposable {
            database.userDao()
                .getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map {
                    it.firstOrNull { player -> player.nick == nick } != null
                }.flatMap { nickExists ->
                    if (nick.trim().isEmpty()) {
                        return@flatMap Single.just(PlayerCreationEvent.NICKNAME_EMPTY)
                    }
                    if (nickExists) {
                        Single.just(PlayerCreationEvent.NICKNAME_EXISTS)
                    } else {
                        database
                            .userDao()
                            .insert(Player(nick))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .andThen(Single.just(PlayerCreationEvent.SUCCESS)
                        )
                    }
                }.subscribe { result ->
                    playerCreationEvent.postValue(result)
                }
        }
    }
}