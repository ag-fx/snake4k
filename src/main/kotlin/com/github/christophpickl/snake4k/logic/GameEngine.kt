package com.github.christophpickl.snake4k.logic

import com.github.christophpickl.snake4k.model.GameState
import com.github.christophpickl.snake4k.model.Settings
import com.github.christophpickl.snake4k.model.State
import com.github.christophpickl.snake4k.view.ExceptionEvent
import com.github.christophpickl.snake4k.view.GameOverEvent
import com.github.christophpickl.snake4k.view.RestartGuiceEvent
import com.github.christophpickl.snake4k.view.components.Board
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import javafx.application.Platform
import javafx.beans.value.ChangeListener
import mu.KotlinLogging
import java.time.Duration
import java.time.LocalDateTime
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

class GameEngine @Inject constructor(
    private val logic: GameLogic,
    private val state: State,
    private val board: Board,
    private val bus: EventBus,
    private val settings: Settings
) {

    init {
        bus.register(this)
    }

    private val log = KotlinLogging.logger {}
    private var timer: Timer? = null
    private var currentTask: GameTimerTask? = null

    fun restart() {
        logic.resetState()
        stop()
        start()
    }

    fun stop() {
        log.info { "stop engine" }
        stopGame()
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe
    fun onRestartEvent(event: RestartGuiceEvent = RestartGuiceEvent()) {
        log.debug { "Received restart event" }
        stop()
    }

    private fun start() {
        log.debug { "start engine" }
        Platform.runLater { state.gameState = GameState.Running }
        timer = Timer(true).also { currentTimer ->
            currentTask = GameTimerTask(state,
                onTick = {
                    val result = logic.onTick()
                    if (result is TickResult.Died) {
                        gameOver(result.message)
                    }
                },
                onUiTick = {
                    board.repaint()
                },
                onException = { e ->
                    currentTimer.cancel()
                    e.printStackTrace()
                    bus.post(ExceptionEvent(e))
                })
            currentTimer.scheduleAtFixedRate(currentTask, 0L, settings.speed.inMs)
        }
    }

    private fun gameOver(detailMessage: String) {
        log.info { "Game over: $detailMessage" }
        stopGame()
        val secondsPlayed = Duration.between(state.timeStarted, LocalDateTime.now()).seconds

        bus.post(GameOverEvent(
            detailMessage = detailMessage,
            fruitsEaten = state.fruitsEaten,
            secondsPlayed = secondsPlayed.toInt()
        ))
    }

    private fun stopGame() {
        Platform.runLater { state.gameState = GameState.NotRunning }
        timer?.cancel()
        currentTask?.unregister()
    }

}

private class GameTimerTask(
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

    init {
        state.gameStateProperty.addListener(gameStateListener)
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
