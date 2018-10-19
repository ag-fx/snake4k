package com.github.christophpickl.snake4k.logic

import com.github.christophpickl.snake4k.model.GameState
import com.github.christophpickl.snake4k.model.State
import javafx.application.Platform
import javafx.beans.value.ChangeListener
import java.util.TimerTask

class GameTimerTask(
    private val state: State,
    private val onTick: () -> Unit,
    private val onUiTick: () -> Unit,
    private val onException: (Exception) -> Unit
) : TimerTask() {

    private var shouldRun = false
    private var shouldRunUiTickOnceMore = false

    @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
    private val gameStateListener = ChangeListener<GameState> { _, _, newValue ->
        when (newValue) {
            GameState.NotRunning -> shouldRun = false
            GameState.Running -> shouldRun = true
            GameState.Paused -> {
                shouldRun = false
                shouldRunUiTickOnceMore = true
            }
        }
    }

    private val tickRunnable = {
        try {
            onTick()
        } catch (e: Exception) {
            onException(e)
        }
    }
    private val uiTickRunnable = {
        try {
            onUiTick()
        } catch (e: Exception) {
            onException(e)
        }
    }

    init {
        state.gameStateProperty.addListener(gameStateListener)
    }

    override fun run() {
        if (shouldRun) {
            tickRunnable()
            Platform.runLater(uiTickRunnable)
        } else if (shouldRunUiTickOnceMore) {
            shouldRunUiTickOnceMore = false
            Platform.runLater(uiTickRunnable)
        }
    }

    fun unregister() {
        state.gameStateProperty.removeListener(gameStateListener)
    }

}
